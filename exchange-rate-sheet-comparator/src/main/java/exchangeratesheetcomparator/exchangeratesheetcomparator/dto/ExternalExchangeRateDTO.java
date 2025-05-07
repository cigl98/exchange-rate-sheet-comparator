package exchangeratesheetcomparator.exchangeratesheetcomparator.dto;

public record ExternalExchangeRateDTO(
        String providerName,
        String currencyPair,
        double rate
) {
}
