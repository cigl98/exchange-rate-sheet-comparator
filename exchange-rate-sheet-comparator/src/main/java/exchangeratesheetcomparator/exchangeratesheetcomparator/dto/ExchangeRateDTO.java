package exchangeratesheetcomparator.exchangeratesheetcomparator.dto;

import exchangeratesheetcomparator.exchangeratesheetcomparator.utils.CurrencyPair;


public record ExchangeRateDTO(
        String providerName,
        CurrencyPair currencyPair,
        double rate
) {
}
