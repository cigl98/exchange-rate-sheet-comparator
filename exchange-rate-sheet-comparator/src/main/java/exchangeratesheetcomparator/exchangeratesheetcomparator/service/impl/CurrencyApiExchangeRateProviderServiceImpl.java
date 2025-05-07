package exchangeratesheetcomparator.exchangeratesheetcomparator.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import exchangeratesheetcomparator.exchangeratesheetcomparator.dto.ExternalExchangeRateDTO;
import exchangeratesheetcomparator.exchangeratesheetcomparator.exception.ExchangeRatesFetchException;
import exchangeratesheetcomparator.exchangeratesheetcomparator.exception.InvalidCurrencyPairException;
import exchangeratesheetcomparator.exchangeratesheetcomparator.service.AllowedCurrencyPairsService;
import exchangeratesheetcomparator.exchangeratesheetcomparator.service.ExternalExchangeRateProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


@Service
public class CurrencyApiExchangeRateProviderServiceImpl implements ExternalExchangeRateProviderService {

    private final AllowedCurrencyPairsService allowedCurrencyPairsService;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${exchange.rates.currency-api.url}")
    private String CURRENCY_API_URL;


    @Autowired
    public CurrencyApiExchangeRateProviderServiceImpl(AllowedCurrencyPairsService allowedCurrencyPairsService) {
        this.allowedCurrencyPairsService = allowedCurrencyPairsService;
        this.restClient = RestClient.create();
        this.objectMapper = new ObjectMapper();
    }


    @Override
    public ExternalExchangeRateDTO fetchExchangeRateForCurrencyPair(String currencyPair) throws ExchangeRatesFetchException {
        if (!allowedCurrencyPairsService.getAllowedCurrencyPairs().allowedCurrencyPairs().contains(currencyPair)) {
            throw new InvalidCurrencyPairException("Currency pair is not supported.");
        }

        CurrencyPair parsedCurrencyPair = parseCurrencyPair(currencyPair);

        try {
            String body = fetchExchangeRatesForCurrency(parsedCurrencyPair.first().toLowerCase());
            JsonNode jsonBody = objectMapper.readTree(body);
            double rate = jsonBody.get(parsedCurrencyPair.first()).get(parsedCurrencyPair.second()).asDouble();
            return new ExternalExchangeRateDTO("Currency-api", currencyPair, rate);
        } catch (Exception e) {
            throw new ExchangeRatesFetchException("Unable to get exchange rate from Currency-api. Reason: " + e.getMessage(), e);
        }
    }


    private String fetchExchangeRatesForCurrency(String currency) {
        return restClient.get()
                .uri(CURRENCY_API_URL.replace("{currencyCode}", currency))
                .retrieve()
                .onStatus(HttpStatusCode::isError, ((request, response) -> {
                    throw new ExchangeRatesFetchException("Unable to get exchange rates from Currency-api. Currency-api endpoint returned: %s %s".formatted(response.getStatusCode(), response.getStatusText()));
                }))
                .body(String.class);
    }


    private CurrencyPair parseCurrencyPair(String currencyPair) {
        String[] split = currencyPair.split("/");
        if (split.length != 2) {
            throw new InvalidCurrencyPairException("Currency pair must be in format {currency1/currency2}.");
        }
        return new CurrencyPair(split[0], split[1]);
    }

    private record CurrencyPair(
            String first,
            String second
    ) {
    }
}
