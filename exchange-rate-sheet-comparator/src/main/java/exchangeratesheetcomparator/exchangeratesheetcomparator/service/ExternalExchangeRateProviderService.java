package exchangeratesheetcomparator.exchangeratesheetcomparator.service;

import exchangeratesheetcomparator.exchangeratesheetcomparator.dto.ExchangeRateDTO;
import exchangeratesheetcomparator.exchangeratesheetcomparator.exception.ExchangeRatesFetchException;
import exchangeratesheetcomparator.exchangeratesheetcomparator.utils.CurrencyPair;


public interface ExternalExchangeRateProviderService {
    ExchangeRateDTO fetchExchangeRateForCurrencyPair(CurrencyPair currencyPair) throws ExchangeRatesFetchException;
}
