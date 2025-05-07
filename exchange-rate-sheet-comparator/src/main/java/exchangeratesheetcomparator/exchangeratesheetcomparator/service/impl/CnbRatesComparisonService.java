package exchangeratesheetcomparator.exchangeratesheetcomparator.service.impl;

import exchangeratesheetcomparator.exchangeratesheetcomparator.dto.ExchangeRateDTO;
import exchangeratesheetcomparator.exchangeratesheetcomparator.dto.ExchangeRatesComparisonDTO;
import exchangeratesheetcomparator.exchangeratesheetcomparator.exception.InvalidCurrencyPairException;
import exchangeratesheetcomparator.exchangeratesheetcomparator.service.CnbExchangeRatesService;
import exchangeratesheetcomparator.exchangeratesheetcomparator.service.ExchangeRatesComparisonService;
import exchangeratesheetcomparator.exchangeratesheetcomparator.service.ExternalExchangeRateProviderService;
import exchangeratesheetcomparator.exchangeratesheetcomparator.utils.CurrencyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CnbRatesComparisonService implements ExchangeRatesComparisonService {
    private final CnbExchangeRatesService cnbExchangeRatesService;
    private final List<ExternalExchangeRateProviderService> externalExchangeRateProviders;


    @Autowired
    public CnbRatesComparisonService(CnbExchangeRatesService cnbExchangeRatesService, List<ExternalExchangeRateProviderService> externalExchangeRateProviders) {
        this.cnbExchangeRatesService = cnbExchangeRatesService;
        this.externalExchangeRateProviders = externalExchangeRateProviders;
    }


    @Override
    public ExchangeRatesComparisonDTO compareExchangeRatesForCurrencyPair(String currencyPair) {
        CurrencyPair parsedCurrencyPair = parseCurrencyPair(currencyPair);

        ExchangeRateDTO cnbExchangeRate = cnbExchangeRatesService.fetchExchangeRates().get(parsedCurrencyPair.base());
        List<ExchangeRateDTO> externalProviderExchangeRates = new ArrayList<>();
        for (var provider : externalExchangeRateProviders) {
            externalProviderExchangeRates.add(provider.fetchExchangeRateForCurrencyPair(parsedCurrencyPair));
        }

        return new ExchangeRatesComparisonDTO(
                parsedCurrencyPair,
                cnbExchangeRate.providerName(),
                cnbExchangeRate.rate(),
                externalProviderExchangeRates.stream()
                        .map(r -> mapRateToComparisonDto(r, cnbExchangeRate))
                        .toList());
    }

    private ExchangeRatesComparisonDTO.RateComparison mapRateToComparisonDto(ExchangeRateDTO externalRate, ExchangeRateDTO cnbRate) {
        return new ExchangeRatesComparisonDTO.RateComparison(
                externalRate.providerName(),
                externalRate.rate(),
                getDifference(externalRate, cnbRate));
    }


    private double getDifference(ExchangeRateDTO externalRate, ExchangeRateDTO cnbRate) {
        return Math.abs(cnbRate.rate() - externalRate.rate());
    }


    private CurrencyPair parseCurrencyPair(String currencyPair) {
        String[] split = currencyPair.split("/");
        if (split.length != 2) {
            throw new InvalidCurrencyPairException("Currency pair must be in format {currency1/currency2}.");
        }
        return new CurrencyPair(split[0].toLowerCase(), split[1].toLowerCase());
    }
}
