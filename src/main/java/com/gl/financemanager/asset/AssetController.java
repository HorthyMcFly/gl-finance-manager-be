package com.gl.financemanager.asset;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/assets")
@AllArgsConstructor
public class AssetController {

  private AssetService assetService;

  @GetMapping
  @ResponseBody
  public ResponseEntity<List<AssetDto>> getAssetsForLoggedInUser() {
    return new ResponseEntity<>(assetService.getAssetsForLoggedInUser(),
        HttpStatusCode.valueOf(200));
  }

  @GetMapping("/asset-types")
  @ResponseBody
  public ResponseEntity<List<AssetType>> getAssetTypes() {
    return new ResponseEntity<>(assetService.getAssetTypes(),
        HttpStatusCode.valueOf(200));
  }

  @PostMapping
  @ResponseBody
  public ResponseEntity<AssetDto> createAsset(@RequestBody @Valid AssetDto assetDto) {
    return new ResponseEntity<>(assetService.createAsset(assetDto),
        HttpStatusCode.valueOf(201));
  }

  @PutMapping("/sell")
  @ResponseBody
  public ResponseEntity<AssetDto> sellAsset(@RequestBody @Valid AssetDto assetDto) {
    return new ResponseEntity<>(assetService.decreaseAssetAmount(assetDto),
        HttpStatusCode.valueOf(200));
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  public ResponseEntity<Void> liquidateAsset(@PathVariable Integer id) {
    assetService.liquidateAsset(id);
    return new ResponseEntity<>(null, HttpStatusCode.valueOf(204));
  }

  @PostMapping("/investment-balance-to-income")
  @ResponseBody
  public ResponseEntity<Void> investmentBalanceToIncome(
      @RequestBody @Valid AssetInvestmentBalanceDto assetInvestmentBalanceDto) {
    assetService.investmentBalanceToIncome(assetInvestmentBalanceDto.getAmount());
    return new ResponseEntity<>(null, HttpStatusCode.valueOf(204));
  }
}
