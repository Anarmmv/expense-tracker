package com.anar.expensetracker.controller;

import com.anar.expensetracker.dto.CategoryTotal;
import com.anar.expensetracker.dto.ExpenseRequest;
import com.anar.expensetracker.entity.ExpenseEntity;
import com.anar.expensetracker.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseEntity> create(@Valid @RequestBody ExpenseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(expenseService.create(
                        request.amount(),
                        request.description(),
                        request.expenseDate(),
                        request.categoryId()));
    }

    @GetMapping
    public ResponseEntity<List<ExpenseEntity>> getAll() {
        return ResponseEntity.ok(expenseService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseEntity> getById(@PathVariable Long id) {
        return ResponseEntity.ok(expenseService.getById(id));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ExpenseEntity>> getByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(expenseService.getByCategory(categoryId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        expenseService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/range")
    public ResponseEntity<List<ExpenseEntity>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(expenseService.getByDateRange(from, to));
    }

    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getTotal(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(expenseService.getTotal(from, to));
    }
    @GetMapping("/summary")
    public ResponseEntity<List<CategoryTotal>> getSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(expenseService.getSummaryByCategory(from, to));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseEntity> update(@PathVariable Long id,
                                                @Valid @RequestBody ExpenseRequest request) {
        return ResponseEntity.ok(expenseService.update(
                id,
                request.amount(),
                request.description(),
                request.expenseDate(),
                request.categoryId()));
    }
}
