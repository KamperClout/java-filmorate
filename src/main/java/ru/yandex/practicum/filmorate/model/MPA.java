package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MPA {
    protected int id;
    protected String name;
}