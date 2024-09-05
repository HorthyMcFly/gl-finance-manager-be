package com.gl.financemanager.expense.repository;

import com.gl.financemanager.expense.entity.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Integer> {

  Optional<ExpenseCategory> findByCategory(String category);
}
