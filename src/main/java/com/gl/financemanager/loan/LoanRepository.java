package com.gl.financemanager.loan;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Integer> {

  List<Loan> findByFmUserUsername(String username);
}
