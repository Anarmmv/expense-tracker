package com.anar.expensetracker.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequest(@NotNull @Positive BigDecimal amount,
                             @Size(max = 255) String description,
                             @NotNull LocalDate expenseDate,
                             @NotNull Long categoryId) {
}
