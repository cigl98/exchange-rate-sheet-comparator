package exchangeratesheetcomparator.exchangeratesheetcomparator.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import exchangeratesheetcomparator.exchangeratesheetcomparator.dto.ExchangeRateDTO;
import exchangeratesheetcomparator.exchangeratesheetcomparator.exception.ExchangeRatesFetchException;
import exchangeratesheetcomparator.exchangeratesheetcomparator.exception.InvalidCurrencyPairException;
import exchangeratesheetcomparator.exchangeratesheetcomparator.service.AllowedCurrencyPairsService;
import exchangeratesheetcomparator.exchangeratesheetcomparator.service.ExternalExchangeRateProviderService;
import exchangeratesheetcomparator.exchangeratesheetcomparator.utils.CurrencyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


@Service
public class CurrencyApiExchangeRateProviderServiceImpl implements ExternalExchangeRateProviderService {

    public static final String CURRENCY_API_PROVIDER_NAME = "Currency-api";
    public static final String CURRENCY_CODE_URL_PARAMETER = "{currencyCode}";

    private final AllowedCurrencyPairsService allowedCurrencyPairsService;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    private final String currencyApiUrl;


    @Autowired
    public CurrencyApiExchangeRateProviderServiceImpl(
            AllowedCurrencyPairsService allowedCurrencyPairsService,
            @Value("${exchange.rates.currency-api.url}") String currencyApiUrl) {

        this.allowedCurrencyPairsService = allowedCurrencyPairsService;
        this.currencyApiUrl = currencyApiUrl;
        this.restClient = RestClient.create();
        this.objectMapper = new ObjectMapper();
    }


    @Override
    public ExchangeRateDTO fetchExchangeRateForCurrencyPair(CurrencyPair currencyPair) throws ExchangeRatesFetchException {
        if (!allowedCurrencyPairsService.getAllowedCurrencyPairs().allowedCurrencyPairs().contains(currencyPair.toString())) {
            throw new InvalidCurrencyPairException("Currency pair is not supported.");
        }

        try {
            String body = fetchExchangeRatesForCurrency(currencyPair.base().toLowerCase());
            JsonNode jsonBody = objectMapper.readTree(body);
            double rate = jsonBody.get(currencyPair.base()).get(currencyPair.other()).asDouble();
            return new ExchangeRateDTO(CURRENCY_API_PROVIDER_NAME, currencyPair, rate);
        } catch (Exception e) {
            throw new ExchangeRatesFetchException("Unable to get exchange rate from Currency-api. Reason: " + e.getMessage(), e);
        }
    }


    private String fetchExchangeRatesForCurrency(String currency) {
        return restClient.get()
                .uri(currencyApiUrl.replace(CURRENCY_CODE_URL_PARAMETER, currency))
                .retrieve()
                .onStatus(HttpStatusCode::isError, ((request, response) -> {
                    throw new ExchangeRatesFetchException("Unable to get exchange rates from Currency-api. Currency-api endpoint returned: %s %s".formatted(response.getStatusCode(), response.getStatusText()));
                }))
                .body(String.class);
    }
}
