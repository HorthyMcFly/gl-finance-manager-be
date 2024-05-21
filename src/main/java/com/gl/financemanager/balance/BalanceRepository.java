package com.gl.financemanager.balance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Integer> {

  Optional<Balance> findBalanceByFmUserUsername(String username);

  Balance findByFmUserId(Integer id);
}
