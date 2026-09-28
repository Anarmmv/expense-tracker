package com.anar.expensetracker.repository;



import com.anar.expensetracker.dto.CategoryTotal;
import com.anar.expensetracker.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

    List<ExpenseEntity> findByCategoryId(Long categoryId);

    List<ExpenseEntity> findByExpenseDateBetween(LocalDate from, LocalDate to);

    @Query("select sum(e.amount) from ExpenseEntity e where e.expenseDate between :from and :to")
    BigDecimal sumByDateRange(@Param("from") LocalDate from, @Param("to") LocalDate to);
    @Query("""
        select new com.anar.expensetracker.dto.CategoryTotal(c.name, sum(e.amount))
        from ExpenseEntity e join e.category c
        where e.expenseDate between :from and :to
        group by c.name
        """)
    List<CategoryTotal> totalsByCategory(@Param("from") LocalDate from, @Param("to") LocalDate to);
    boolean existsByCategoryId(Long categoryId);
}