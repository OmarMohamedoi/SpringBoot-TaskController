package com.practise.springboot1.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record TaskDTO(
        @NotBlank(message = "Title can't be empty.")
        String title,
        String description,
        boolean completed,
        LocalDate dueDate) {
}
