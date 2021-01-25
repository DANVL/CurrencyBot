package com.currencybot.services.impl;

import com.currencybot.config.ConfigStrings;
import com.currencybot.entities.Exchanger;
import com.currencybot.dto.SourceDto;
import com.currencybot.entities.Currency;
import com.currencybot.entities.Rate;
import com.currencybot.entities.Source;
import com.currencybot.exceptions.NoExchangersException;
import com.currencybot.exceptions.NotSupportedCurrencyException;
import com.currencybot.services.CurrencyService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final RestTemplate restTemplate;

    public CurrencyServiceImpl() {
        this.restTemplate = new RestTemplate();
    }


    @Override
    public Rate getCurrencyRate(Currency currency, Source source)
            throws NotSupportedCurrencyException, NoExchangersException {

        SourceDto result = restTemplate
                .getForObject(source == Source.MONEY24 ? ConfigStrings.MONEY24_URI : ConfigStrings.BANKS_URI,
                        SourceDto.class);

        if (result != null) {
            return processResult(result, currency, source);
        }

        throw new NoExchangersException();
    }

    private Rate processResult(SourceDto sourceDto, Currency currency, Source source)
            throws NotSupportedCurrencyException, NoExchangersException {

        List<Exchanger> exchangers = sourceDto.getExchangers();
        Optional<Exchanger> exchanger = exchangers
                .stream()
                .filter(x -> x.getId() == source.getValue())
                .findAny();

        if (exchanger.isPresent()) {
            switch (currency) {
                case eur:
                    return exchanger.get().getRates().getEur();
                case rur:
                    return exchanger.get().getRates().getRur();
                case usd:
                    return exchanger.get().getRates().getUsd();
                default:
                    throw new NotSupportedCurrencyException();
            }
        }

        throw new NoExchangersException();
    }
}
