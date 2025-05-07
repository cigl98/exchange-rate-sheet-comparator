package exchangeratesheetcomparator.exchangeratesheetcomparator.controller;

import exchangeratesheetcomparator.exchangeratesheetcomparator.dto.AllowedCurrencyPairsDTO;
import exchangeratesheetcomparator.exchangeratesheetcomparator.dto.ExchangeRatesComparisonDTO;
import exchangeratesheetcomparator.exchangeratesheetcomparator.service.AllowedCurrencyPairsService;
import exchangeratesheetcomparator.exchangeratesheetcomparator.service.ExchangeRatesComparisonService;
import exchangeratesheetcomparator.exchangeratesheetcomparator.service.impl.ClasspathAllowedCurrencyPairsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class ApiController {

    private final AllowedCurrencyPairsService allowedCurrencyPairsService;
    private final ExchangeRatesComparisonService exchangeRatesComparisonService;

    @Autowired
    public ApiController(ClasspathAllowedCurrencyPairsServiceImpl allowedCurrencyPairsService, ExchangeRatesComparisonService exchangeRatesComparisonService) {
        this.allowedCurrencyPairsService = allowedCurrencyPairsService;
        this.exchangeRatesComparisonService = exchangeRatesComparisonService;
    }


    @GetMapping("/rates-compare")
    public ResponseEntity<ExchangeRatesComparisonDTO> getRate(@RequestParam String currencyPair) {
        return ResponseEntity.ok((exchangeRatesComparisonService.compareExchangeRatesForCurrencyPair(currencyPair)));
    }

    @GetMapping("/allowed-pairs")
    public ResponseEntity<AllowedCurrencyPairsDTO> getAllowedCurrencyPairs() {
        return ResponseEntity.ok(allowedCurrencyPairsService.getAllowedCurrencyPairs());
    }
}
