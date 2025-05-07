package exchangeratesheetcomparator.exchangeratesheetcomparator.dto;

import exchangeratesheetcomparator.exchangeratesheetcomparator.utils.CurrencyPair;

import java.util.List;


public record ExchangeRatesComparisonDTO(
        CurrencyPair pair,
        String originalProviderName,
        Double originalProviderRate,
        List<RateComparison> comparisons) {

    public record RateComparison(
            String providerName,
            Double rate,
            Double difference
    ) {
    }
}
