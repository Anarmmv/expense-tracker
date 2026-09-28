package com.anar.expensetracker.service;

import com.anar.expensetracker.entity.CategoryEntity;
import com.anar.expensetracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryEntity create(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category already exists: " + name);
        }
        var category = CategoryEntity.builder().name(name).build();
        return categoryRepository.save(category);
    }

    public List<CategoryEntity> getAll() {
        return categoryRepository.findAll();
    }

    public CategoryEntity getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Category not found with id: " + id));
    }

    public void delete(Long id) {
        var category = getById(id);
        categoryRepository.delete(category);
    }

        public CategoryEntity update(Long id, String name) {
            var category = getById(id);
            category.setName(name);
            return categoryRepository.save(category);
        }
    }

