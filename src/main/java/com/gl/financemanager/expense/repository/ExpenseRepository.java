package com.gl.financemanager.expense.repository;

import com.gl.financemanager.expense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

  List<Expense> findByFmUserUsernameAndFmPeriodId(String username, Integer periodId);

  Expense findByLoanIdAndFmPeriodId(Integer loanId, Integer periodId);

  List<Expense> findAllByLoanId(Integer loanId);
}
