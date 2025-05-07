package exchangeratesheetcomparator.exchangeratesheetcomparator.dto;

import java.util.Set;

public record AllowedCurrencyPairsDTO(
        Set<String> allowedCurrencyPairs
) {
}
