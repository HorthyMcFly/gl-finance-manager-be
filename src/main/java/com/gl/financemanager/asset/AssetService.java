package com.gl.financemanager.asset;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AssetService {

  private AssetRepository assetRepository;

  private AssetTypeRepository assetTypeRepository;

  public List<AssetDto> getAssetsForLoggedInUser() {
    var loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    return assetRepository.findByFmUserUsername(loggedInUsername)
        .stream().map(AssetService::toDto).toList();
  }

  public List<AssetType> getAssetTypes() {
    return assetTypeRepository.findAll();
  }

  static AssetDto toDto(Asset asset) {
    return AssetDto.builder()
        .id(asset.getId())
        .amount(asset.getAmount())
        .name(asset.getName())
        .assetType(asset.getAssetType())
        .maturityDate(asset.getMaturityDate())
        .interestRate(asset.getInterestRate())
        .interestPaymentDate(asset.getInterestPaymentDate())
        .build();
  }
}
