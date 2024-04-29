package com.gl.financemanager.asset;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/assets")
@AllArgsConstructor
public class AssetController {

  private AssetService assetService;

  @GetMapping
  @ResponseBody
  public List<AssetDto> getAssetsForLoggedInUser() {
    return assetService.getAssetsForLoggedInUser();
  }

  @GetMapping("/asset-types")
  @ResponseBody
  public List<AssetType> getAssetTypes() {
    return assetService.getAssetTypes();
  }
}
