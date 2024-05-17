package com.gl.financemanager.asset;

import com.gl.financemanager.auth.UserRepository;
import com.gl.financemanager.balance.BalanceService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AssetService {

  private BalanceService balanceService;

  private AssetRepository assetRepository;
  private AssetTypeRepository assetTypeRepository;
  private UserRepository userRepository;

  public List<AssetDto> getAssetsForLoggedInUser() {
    var loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    return assetRepository.findByFmUserUsername(loggedInUsername)
        .stream().map(AssetService::toDto).toList();
  }

  public List<AssetType> getAssetTypes() {
    return assetTypeRepository.findAll();
  }

  @Transactional
  public AssetDto createAsset(AssetDto assetDto) {
    if (assetDto.getId() != null) {
      throw new RuntimeException();
    }
    var newAsset = AssetService.fromDto(assetDto);
    var loggedInUser = userRepository
        .findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    if (loggedInUser.isEmpty()) {
      throw new RuntimeException();
    }
    newAsset.setFmUser(loggedInUser.get());
    var createdAsset = assetRepository.save(newAsset);
    if (assetDto.getUseInvestmentBalance()) {
      balanceService.updateInvestmentBalanceForLoggedInUser(assetDto.getAmount().negate());
    }
    return AssetService.toDto(createdAsset);
  }

  static AssetDto toDto(Asset asset) {
    return AssetDto.builder()
        .id(asset.getId())
        .amount(asset.getAmount())
        .name(asset.getName())
        .assetType(asset.getAssetType())
        .maturityDate(asset.getMaturityDate())
        .interestRate(asset.getInterestRate())
        .interestPaymentMonth(asset.getInterestPaymentMonth())
        .build();
  }

  static Asset fromDto(AssetDto assetDto) {
    return Asset.builder()
        .amount(assetDto.getAmount())
        .name(assetDto.getName())
        .assetType(assetDto.getAssetType())
        .maturityDate(assetDto.getMaturityDate())
        .interestRate(assetDto.getInterestRate())
        .interestPaymentMonth(assetDto.getInterestPaymentMonth())
        .build();
  }
}
