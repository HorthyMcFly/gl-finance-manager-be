package com.gl.financemanager.asset;

import com.gl.financemanager.auth.UserRepository;
import com.gl.financemanager.balance.BalanceService;
import com.gl.financemanager.income.IncomeDto;
import com.gl.financemanager.income.IncomeService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class AssetService {

  private BalanceService balanceService;
  private IncomeService incomeService;

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
    var userBalance = balanceService.getBalanceForLoggedInUser();
    if (assetDto.getUseInvestmentBalance() &&
        (assetDto.getAmount().compareTo(userBalance.getInvestmentBalance()) > 0)) {
      throw new RuntimeException();
    }
    newAsset.setFmUser(loggedInUser.get());
    var createdAsset = assetRepository.save(newAsset);
    if (assetDto.getUseInvestmentBalance()) {
      balanceService.updateInvestmentBalanceForLoggedInUser(assetDto.getAmount().negate());
    }
    return AssetService.toDto(createdAsset);
  }

  @Transactional
  public AssetDto decreaseAssetAmount(AssetDto assetDto) {
    var existingAsset = this.findExistingAssetIfValidId(assetDto.getId());
    // can't increase amount with this method
    if (assetDto.getAmount().compareTo(existingAsset.getAmount()) >= 0 ) {
      throw new RuntimeException();
    }
    var amountDifference = existingAsset.getAmount().subtract(assetDto.getAmount());
    existingAsset.setAmount(assetDto.getAmount());
    var modifiedAsset = assetRepository.save(existingAsset);
    balanceService.updateInvestmentBalanceForLoggedInUser(amountDifference);
    return AssetService.toDto(modifiedAsset);
  }

  @Transactional
  public void liquidateAsset(Integer assetId) {
    var existingAsset = this.findExistingAssetIfValidId(assetId);
    var fullAmount = existingAsset.getAmount();
    assetRepository.delete(existingAsset);
    balanceService.updateInvestmentBalanceForLoggedInUser(fullAmount);
  }

  @Transactional
  public void investmentBalanceToIncome(BigDecimal amount) {
    var userBalance = balanceService.getBalanceForLoggedInUser();
    if (amount.compareTo(userBalance.getInvestmentBalance()) > 0) {
      throw new RuntimeException();
    }
    balanceService.updateInvestmentBalanceForLoggedInUser(amount.negate());
    var incomeDto = IncomeDto.builder()
        .amount(amount)
        .source("Befektetési egyenleg")
        .build();
    incomeService.createIncome(incomeDto, false);
  }

  private Asset findExistingAssetIfValidId(Integer id) {
    if (id == null) {
      throw new RuntimeException();
    }
    var loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    var existingAssetOpt = assetRepository.findById(id);
    if (existingAssetOpt.isEmpty()) {
      throw new RuntimeException();
    }
    var existingAsset = existingAssetOpt.get();
    if (!existingAsset.getFmUser().getUsername().equals(loggedInUsername)) {
      throw new RuntimeException();
    }

    return existingAsset;
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
