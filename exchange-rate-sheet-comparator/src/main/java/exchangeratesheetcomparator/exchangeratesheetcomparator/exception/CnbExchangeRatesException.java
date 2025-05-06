package exchangeratesheetcomparator.exchangeratesheetcomparator.exception;

public class CnbExchangeRatesException extends RuntimeException {

    public CnbExchangeRatesException(String message) {
        super(message);
    }

    public CnbExchangeRatesException(String message, Throwable cause) {
        super(message, cause);
    }
}
