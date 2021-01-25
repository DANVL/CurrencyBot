package com.currencybot.dto;

import com.currencybot.entities.Exchanger;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SourceDto {
    private List<Exchanger> exchangers;
}

