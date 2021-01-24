package com.currencybot.entities;

import com.currencybot.bot.BotState;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "id")} )
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "id")
    @NotNull
    private Integer id;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "bot_state")
    private BotState botState;
}
