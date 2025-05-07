package exchangeratesheetcomparator.exchangeratesheetcomparator.service.impl;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import exchangeratesheetcomparator.exchangeratesheetcomparator.dto.CnbExchangeRatesDTO;
import exchangeratesheetcomparator.exchangeratesheetcomparator.exception.ExchangeRatesFetchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest(httpPort = 8089)
class XmlCnbExchangeRatesServiceImplTest {

    private static final String EXCHANGE_RATES_URL = "http://localhost:8089/financni_trhy/devizovy_trh/kurzy_devizoveho_trhu/denni_kurz.xml";

    private XmlCnbExchangeRatesServiceImpl service;

    private static final String BODY_XML = """
        <?xml version="1.0" encoding="UTF-8"?>
        <kurzy banka="CNB" datum="06.05.2025" poradi="86">
            <tabulka typ="XML_TYP_CNB_KURZY_DEVIZOVEHO_TRHU">
                <radek kod="EUR" mena="euro" mnozstvi="1" kurz="24,960" zeme="EMU"/>
                <radek kod="USD" mena="dolar" mnozstvi="1" kurz="22,031" zeme="USA"/>
                <radek kod="CAD" mena="dolar" mnozstvi="1" kurz="15,958" zeme="Kanada"/>
                <radek kod="JPY" mena="jen" mnozstvi="100" kurz="15,427" zeme="Japonsko"/>
            </tabulka>
        </kurzy>
        """;

    @BeforeEach
    void setUp() {
        service = new XmlCnbExchangeRatesServiceImpl(EXCHANGE_RATES_URL);
    }

    @Test
    void fetchExchangeRates_shouldSucceed() {
        stubFor(get(urlEqualTo("/financni_trhy/devizovy_trh/kurzy_devizoveho_trhu/denni_kurz.xml"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/xml")
                        .withBody(BODY_XML)));

        CnbExchangeRatesDTO dto = service.fetchExchangeRates();

        assertNotNull(dto);
        assertEquals(4, dto.getTable().getRows().size());
        assertEquals("24,960", dto.getTable().getRows().get(0).getRate());
        assertEquals("22,031", dto.getTable().getRows().get(1).getRate());
        assertEquals("15,958", dto.getTable().getRows().get(2).getRate());
        assertEquals("15,427", dto.getTable().getRows().get(3).getRate());
        assertEquals(100, dto.getTable().getRows().get(3).getAmount());

    }

    @Test
    void fetchExchangeRates_errorStatusReturned_shouldThrow() {
        stubFor(get(urlEqualTo("/financni_trhy/devizovy_trh/kurzy_devizoveho_trhu/denni_kurz.xml"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withBody("Internal Server Error")));

        assertThrows(ExchangeRatesFetchException.class, service::fetchExchangeRates);
    }

    @Test
    void fetchExchangeRates_invalidResponse_shouldThrow() {
        stubFor(get(urlEqualTo("/financni_trhy/devizovy_trh/kurzy_devizoveho_trhu/denni_kurz.xml"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-type", "application/xml").withBody("Body")));

        assertThrows(ExchangeRatesFetchException.class, service::fetchExchangeRates);
    }
}