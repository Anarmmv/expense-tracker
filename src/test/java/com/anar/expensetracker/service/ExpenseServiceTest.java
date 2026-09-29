package com.anar.expensetracker.service;

import com.anar.expensetracker.entity.CategoryEntity;
import com.anar.expensetracker.entity.ExpenseEntity;
import com.anar.expensetracker.repository.ExpenseRepository;
import com.anar.expensetracker.service.CategoryService;
import com.anar.expensetracker.service.ExpenseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock ExpenseRepository expenseRepository;
    @Mock
    CategoryService categoryService;
    @InjectMocks
    ExpenseService expenseService;

    @Test
    void create_savesExpenseWithCategory() {
        var category = CategoryEntity.builder().id(1L).name("Food").build();
        when(categoryService.getById(1L)).thenReturn(category);
        when(expenseRepository.save(any(ExpenseEntity.class))).thenAnswer(i -> i.getArgument(0));

        var result = expenseService.create(new BigDecimal("12.50"), "Bread", LocalDate.of(2026, 9, 28), 1L);

        assertEquals("Food", result.getCategory().getName());
        assertEquals(new BigDecimal("12.50"), result.getAmount());
        verify(expenseRepository).save(any(ExpenseEntity.class));
    }

    @Test
    void getById_throwsNotFound_whenMissing() {
        when(expenseRepository.findById(99L)).thenReturn(Optional.empty());

        var ex = assertThrows(ResponseStatusException.class, () -> expenseService.getById(99L));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void getTotal_returnsZero_whenNoExpenses() {
        var from = LocalDate.of(2026, 1, 1);
        var to = LocalDate.of(2026, 1, 31);
        when(expenseRepository.sumByDateRange(from, to)).thenReturn(null);

        assertEquals(BigDecimal.ZERO, expenseService.getTotal(from, to));
    }
}