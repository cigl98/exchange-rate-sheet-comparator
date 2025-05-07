package exchangeratesheetcomparator.exchangeratesheetcomparator.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import exchangeratesheetcomparator.exchangeratesheetcomparator.dto.AllowedCurrencyPairsDTO;
import exchangeratesheetcomparator.exchangeratesheetcomparator.exception.UnableToLoadCurrencyPairsException;
import exchangeratesheetcomparator.exchangeratesheetcomparator.service.AllowedCurrencyPairsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class ClasspathAllowedCurrencyPairsServiceImpl implements AllowedCurrencyPairsService {

    private static final String ALLOWED_CURRENCY_PAIRS_JSON_PATH = "classpath:allowed-currency-pairs.json";
    
    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;


    @Autowired
    public ClasspathAllowedCurrencyPairsServiceImpl(ObjectMapper objectMapper, ResourceLoader resourceLoader) {
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
    }


    public AllowedCurrencyPairsDTO getAllowedCurrencyPairs() throws UnableToLoadCurrencyPairsException {
        try {
            Resource resource = resourceLoader.getResource(ALLOWED_CURRENCY_PAIRS_JSON_PATH);
            return objectMapper.readValue(resource.getInputStream(), AllowedCurrencyPairsDTO.class);
        } catch (IOException e) {
            throw new UnableToLoadCurrencyPairsException("Failed to read allowed currency pairs, reason: " + e.getMessage(), e);
        }
    }
}
