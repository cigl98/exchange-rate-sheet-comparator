package exchangeratesheetcomparator.exchangeratesheetcomparator.service.impl;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import exchangeratesheetcomparator.exchangeratesheetcomparator.dto.AllowedCurrencyPairsDTO;
import exchangeratesheetcomparator.exchangeratesheetcomparator.dto.ExternalExchangeRateDTO;
import exchangeratesheetcomparator.exchangeratesheetcomparator.exception.ExchangeRatesFetchException;
import exchangeratesheetcomparator.exchangeratesheetcomparator.exception.InvalidCurrencyPairException;
import exchangeratesheetcomparator.exchangeratesheetcomparator.service.AllowedCurrencyPairsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WireMockTest(httpPort = 8090)
class CurrencyApiExchangeRateProviderServiceImplTest {

    public static final String CURRENCY_API_URL = "http://localhost:8090/currencies/{currencyCode}.json";
    private CurrencyApiExchangeRateProviderServiceImpl service;
    private AllowedCurrencyPairsService allowedCurrencyPairsService;

    private static final String BODY_JSON = """
        {
            "eur": {
                "czk": 24.92280708
            }
        }
        """;

    @BeforeEach
    void setUp() {
        allowedCurrencyPairsService = mock(AllowedCurrencyPairsService.class);
        service = new CurrencyApiExchangeRateProviderServiceImpl(allowedCurrencyPairsService, CURRENCY_API_URL);
    }

    @Test
    void fetchExchangeRateForCurrencyPair_shouldSucceed() {
        when(allowedCurrencyPairsService.getAllowedCurrencyPairs())
                .thenReturn(new AllowedCurrencyPairsDTO(Set.of("eur/czk")));

        stubFor(get(urlEqualTo("/currencies/eur.json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(BODY_JSON)));

        ExternalExchangeRateDTO dto = service.fetchExchangeRateForCurrencyPair("eur/czk");

        assertNotNull(dto);
        assertEquals("Currency-api", dto.providerName());
        assertEquals("eur/czk", dto.currencyPair());
        assertEquals(24.92280708, dto.rate());
    }

    @Test
    void fetchExchangeRateForCurrencyPair_invalidCurrencyPair_shouldThrow() {
        when(allowedCurrencyPairsService.getAllowedCurrencyPairs())
                .thenReturn(new AllowedCurrencyPairsDTO(Set.of("eur/czk")));

        assertThrows(InvalidCurrencyPairException.class,
                () -> service.fetchExchangeRateForCurrencyPair("usd/czk")
        );
    }

    @Test
    void fetchExchangeRateForCurrencyPair_errorStatusReturned_shouldThrow() {
        when(allowedCurrencyPairsService.getAllowedCurrencyPairs())
                .thenReturn(new AllowedCurrencyPairsDTO(Set.of("eur/czk")));

        stubFor(get(urlEqualTo("/currencies/eur.json"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withBody("Internal Server Error")));

        assertThrows(ExchangeRatesFetchException.class,
                () -> service.fetchExchangeRateForCurrencyPair("eur/czk")
        );
    }

    @Test
    void fetchExchangeRateForCurrencyPair_invalidResponseBody_shouldThrow() {
        when(allowedCurrencyPairsService.getAllowedCurrencyPairs())
                .thenReturn(new AllowedCurrencyPairsDTO(Set.of("eur/czk")));

        stubFor(get(urlEqualTo("/currencies/eur.json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{ Body }")));

        assertThrows(ExchangeRatesFetchException.class,
                () -> service.fetchExchangeRateForCurrencyPair("eur/czk")
        );
    }
}