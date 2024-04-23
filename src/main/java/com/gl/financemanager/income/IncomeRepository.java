package com.gl.financemanager.income;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Integer> {

  List<Income> findByFmUserUsernameAndFmPeriodId(String username, Integer periodId);
}
