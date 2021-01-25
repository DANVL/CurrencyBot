package com.currencybot.services.impl;

import com.currencybot.dto.Exchanger;
import com.currencybot.dto.SourceDto;
import com.currencybot.entities.Currency;
import com.currencybot.entities.Source;
import com.currencybot.services.CurrencyService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final String NOT_FOUND = "Не найдено";
    private final String money24Uri = "https://kurstoday.com.ua/api/service/kharkiv";
    private final String banksUri = "https://kurstoday.com.ua/api/service/banks-of-ukraine";


    private final RestTemplate restTemplate;

    public CurrencyServiceImpl() {
        this.restTemplate = new RestTemplate();
    }


    @Override
    public String getCurrencyRate(Currency currency, Source source) {
        SourceDto result = restTemplate
                .getForObject(source == Source.MONEY24 ? money24Uri : banksUri,
                        SourceDto.class);

        if (result != null) {
            return processResult(result, currency, source);
        } else {
            return NOT_FOUND;
        }
    }

    private String processResult(SourceDto sourceDto, Currency currency, Source source) {
        List<Exchanger> exchangers = sourceDto.getExchangers();
        Optional<Exchanger> exchanger = exchangers
                .stream()
                .filter(x -> x.getId() == source.getValue())
                .findAny();

        if (exchanger.isPresent()) {
            switch (currency) {
                case eur:
                    return formatAnswer(exchanger.get().getRates().getEur().toString(), currency);
                case rur:
                    return formatAnswer(exchanger.get().getRates().getRur().toString(), currency);
                case usd:
                    return formatAnswer(exchanger.get().getRates().getUsd().toString(), currency);
                default:
                    return NOT_FOUND;
            }
        }

        return NOT_FOUND;
    }

    private String formatAnswer(String base, Currency currency) {
        return String.format("Покупка\\Продажа %s:\n%s", currency, base);
    }
}
