package exchangeratesheetcomparator.exchangeratesheetcomparator.dto;

import java.util.List;


public record AllowedCurrencyPairsDTO(
        List<String> allowedCurrencyPairs
) {
}
