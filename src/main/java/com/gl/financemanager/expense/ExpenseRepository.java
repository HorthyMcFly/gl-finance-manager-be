package com.gl.financemanager.expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

  List<Expense> findByFmUserUsernameAndFmPeriodId(String username, Integer periodId);
}
