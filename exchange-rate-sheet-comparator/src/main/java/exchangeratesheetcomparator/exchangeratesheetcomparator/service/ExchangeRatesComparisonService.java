package exchangeratesheetcomparator.exchangeratesheetcomparator.service;

import exchangeratesheetcomparator.exchangeratesheetcomparator.dto.ExchangeRatesComparisonDTO;


public interface ExchangeRatesComparisonService {

    ExchangeRatesComparisonDTO compareExchangeRatesForCurrencyPair(String currencyPair);

}
