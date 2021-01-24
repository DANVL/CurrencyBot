package com.currencybot.entities;

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
    @Column(name = "id", unique = true, nullable = false)
    @NotNull
    private Integer id;

    @Column(name = "name", unique = true, nullable = false)
    @NotBlank
    private String name;

    @Column(name = "surname", nullable = true)
    private String surname;
}
