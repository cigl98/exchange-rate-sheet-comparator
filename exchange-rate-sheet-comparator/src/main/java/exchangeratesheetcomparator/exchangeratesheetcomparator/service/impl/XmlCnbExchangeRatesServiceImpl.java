package exchangeratesheetcomparator.exchangeratesheetcomparator.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import exchangeratesheetcomparator.exchangeratesheetcomparator.dto.CnbExchangeRatesDTO;
import exchangeratesheetcomparator.exchangeratesheetcomparator.dto.ExchangeRateDTO;
import exchangeratesheetcomparator.exchangeratesheetcomparator.exception.ExchangeRatesFetchException;
import exchangeratesheetcomparator.exchangeratesheetcomparator.service.CnbExchangeRatesService;
import exchangeratesheetcomparator.exchangeratesheetcomparator.utils.CurrencyPair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.stream.Collectors;


@Service
public class XmlCnbExchangeRatesServiceImpl implements CnbExchangeRatesService {

    public static final String PROVIDER_NAME = "CNB";
    private final RestClient restClient;
    private final XmlMapper xmlMapper;

    private final String cnbExchangeRatesUrl;


    public XmlCnbExchangeRatesServiceImpl(
            @Value( "${exchange.rates.cnb.url}") String cnbExchangeRatesUrl) {

        this.cnbExchangeRatesUrl = cnbExchangeRatesUrl;
        this.restClient = RestClient.create();
        xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public Map<String, ExchangeRateDTO> fetchExchangeRates() throws ExchangeRatesFetchException {
        String body = restClient
                .get()
                .uri(cnbExchangeRatesUrl)
                .retrieve()
                .onStatus(HttpStatusCode::isError, ((request, response) -> {
                    throw new ExchangeRatesFetchException("Unable to get exchange rates from CNB. CNB endpoint returned: %s %s".formatted(response.getStatusCode(), response.getStatusText()));
                }))
                .body(String.class);
        try {
            CnbExchangeRatesDTO parsedXml =  xmlMapper.readValue(body, CnbExchangeRatesDTO.class);
            return parsedXml.getTable().getRows().stream().collect(Collectors.toMap(row -> row.getCode().toLowerCase(), this::mapRowToDto));
        } catch (Exception e) {
            throw new ExchangeRatesFetchException("Unable to parse response from CNB, reason: " + e.getMessage(), e);
        }
    }

    private ExchangeRateDTO mapRowToDto(CnbExchangeRatesDTO.Row row) {
        double parsedRate = Double.parseDouble(row.getRate().replace(',', '.')) / row.getAmount();
        return new ExchangeRateDTO(PROVIDER_NAME, new CurrencyPair(row.getCode().toLowerCase(), "czk"), parsedRate);
    }
}
