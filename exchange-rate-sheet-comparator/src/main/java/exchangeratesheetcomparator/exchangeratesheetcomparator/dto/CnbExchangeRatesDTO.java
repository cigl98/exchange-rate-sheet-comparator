package exchangeratesheetcomparator.exchangeratesheetcomparator.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class CnbExchangeRatesDTO {

    @JacksonXmlProperty(localName = "tabulka")
    private Table table;

    @Getter
    @Setter
    public static final class Table {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "radek")
        private List<Row> rows;
    }

    @Getter
    @Setter
    public static final class Row {
        @JacksonXmlProperty(isAttribute = true, localName = "kod")
        private String code;

        @JacksonXmlProperty(isAttribute = true, localName = "mnozstvi")
        private String amount;

        @JacksonXmlProperty(isAttribute = true, localName = "kurz")
        private String rate;
    }
}
