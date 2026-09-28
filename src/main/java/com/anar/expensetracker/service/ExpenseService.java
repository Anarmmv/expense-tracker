package com.anar.expensetracker.service;

import com.anar.expensetracker.dto.CategoryTotal;
import com.anar.expensetracker.entity.ExpenseEntity;
import com.anar.expensetracker.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryService categoryService;

    public ExpenseEntity create(BigDecimal amount, String description,
                                LocalDate expenseDate, Long categoryId) {
        var category = categoryService.getById(categoryId);
        var expense = ExpenseEntity.builder()
                .amount(amount)
                .description(description)
                .expenseDate(expenseDate)
                .category(category)
                .build();
        return expenseRepository.save(expense);
    }

    public List<ExpenseEntity> getAll() {
        return expenseRepository.findAll();
    }

    public ExpenseEntity getById(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Expense not found with id: " + id));
    }

    public List<ExpenseEntity> getByCategory(Long categoryId) {
        return expenseRepository.findByCategoryId(categoryId);
    }

    public void delete(Long id) {
        var expense = getById(id);
        expenseRepository.delete(expense);
    }

    public List<ExpenseEntity> getByDateRange(LocalDate from, LocalDate to) {
        return expenseRepository.findByExpenseDateBetween(from, to);
    }

    public BigDecimal getTotal(LocalDate from, LocalDate to) {
        var total = expenseRepository.sumByDateRange(from, to);
        return total == null ? BigDecimal.ZERO : total;
    }

    public List<CategoryTotal> getSummaryByCategory(LocalDate from, LocalDate to) {
        return expenseRepository.totalsByCategory(from, to);
    }
    public ExpenseEntity update(Long id, BigDecimal amount, String description,
                                LocalDate expenseDate, Long categoryId) {
        var expense = getById(id);
        var category = categoryService.getById(categoryId);

        expense.setAmount(amount);
        expense.setDescription(description);
        expense.setExpenseDate(expenseDate);
        expense.setCategory(category);

        return expenseRepository.save(expense);
    }
}