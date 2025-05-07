package exchangeratesheetcomparator.exchangeratesheetcomparator.exception;

public class ExchangeRatesFetchException extends RuntimeException {

    public ExchangeRatesFetchException(String message) {
        super(message);
    }

    public ExchangeRatesFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
