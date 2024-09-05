package com.gl.financemanager.expense.repository;

import com.gl.financemanager.expense.entity.ExpenseCategoryLimit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseCategoryLimitRepository extends JpaRepository<ExpenseCategoryLimit, Integer> {

  List<ExpenseCategoryLimit> findAllByFmUserUsername(String username);
}
