package exchangeratesheetcomparator.exchangeratesheetcomparator.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import exchangeratesheetcomparator.exchangeratesheetcomparator.dto.CnbExchangeRatesDTO;
import exchangeratesheetcomparator.exchangeratesheetcomparator.exception.ExchangeRatesFetchException;
import exchangeratesheetcomparator.exchangeratesheetcomparator.service.CnbExchangeRatesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


@Service
public class XmlCnbExchangeRatesServiceImpl implements CnbExchangeRatesService {

    private final RestClient restClient;
    private final XmlMapper xmlMapper;

    @Value( "${exchange.rates.cnb.url}")
    private String CNB_EXCHANGE_RATES_URL;


    public XmlCnbExchangeRatesServiceImpl() {
        this.restClient = RestClient.create();
        xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public CnbExchangeRatesDTO fetchExchangeRates() throws ExchangeRatesFetchException {
        String body = restClient
                .get()
                .uri(CNB_EXCHANGE_RATES_URL)
                .retrieve()
                .onStatus(HttpStatusCode::isError, ((request, response) -> {
                    throw new ExchangeRatesFetchException("Unable to get exchange rates from CNB. CNB endpoint returned: %s %s".formatted(response.getStatusCode(), response.getStatusText()));
                }))
                .body(String.class);
        try {
            return xmlMapper.readValue(body, CnbExchangeRatesDTO.class);
        } catch (Exception e) {
            throw new ExchangeRatesFetchException("Unable to parse response from CNB, reason: " + e.getMessage(), e);
        }
    }
}
