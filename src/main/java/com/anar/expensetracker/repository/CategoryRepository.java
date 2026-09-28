package com.anar.expensetracker.repository;

import com.anar.expensetracker.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity,Long> {
    boolean existsByName(String name);
}
