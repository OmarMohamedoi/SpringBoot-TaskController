package com.practise.springboot1.dto;

import java.time.LocalDate;

public record TaskPatchDto(
        String title,
        String description,
        Boolean completed,
        LocalDate dueDate
) {
}
