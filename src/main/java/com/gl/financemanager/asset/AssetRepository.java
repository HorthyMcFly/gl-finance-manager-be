package com.gl.financemanager.asset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Integer> {

  List<Asset> findByFmUserUsername(String username);
}
