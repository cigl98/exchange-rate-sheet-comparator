package exchangeratesheetcomparator.exchangeratesheetcomparator.utils;

import org.springframework.lang.NonNull;


public record CurrencyPair(
        String base,
        String other
) {

    @Override
    @NonNull
    public String toString() {
        return base + "/" + other;
    }
}
