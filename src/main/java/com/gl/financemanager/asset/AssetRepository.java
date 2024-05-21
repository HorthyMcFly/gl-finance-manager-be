package com.gl.financemanager.asset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Integer> {

  List<Asset> findByFmUserUsername(String username);

  List<Asset> findAllByInterestPaymentMonth(Integer monthIndex);

  List<Asset> findAllByMaturityDateBetween(LocalDate start, LocalDate end);
}
