package exchangeratesheetcomparator.exchangeratesheetcomparator.service;

import exchangeratesheetcomparator.exchangeratesheetcomparator.dto.AllowedCurrencyPairsDTO;
import exchangeratesheetcomparator.exchangeratesheetcomparator.exception.UnableToLoadCurrencyPairsException;


public interface AllowedCurrencyPairsService {
    AllowedCurrencyPairsDTO getAllowedCurrencyPairs() throws UnableToLoadCurrencyPairsException;
}
