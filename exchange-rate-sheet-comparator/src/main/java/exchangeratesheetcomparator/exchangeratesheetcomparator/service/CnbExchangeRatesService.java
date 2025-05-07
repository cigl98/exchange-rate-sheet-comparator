package exchangeratesheetcomparator.exchangeratesheetcomparator.service;

import exchangeratesheetcomparator.exchangeratesheetcomparator.dto.CnbExchangeRatesDTO;
import exchangeratesheetcomparator.exchangeratesheetcomparator.dto.ExchangeRateDTO;
import exchangeratesheetcomparator.exchangeratesheetcomparator.exception.ExchangeRatesFetchException;

import java.util.Map;


public interface CnbExchangeRatesService {
    Map<String, ExchangeRateDTO> fetchExchangeRates() throws ExchangeRatesFetchException;
}
