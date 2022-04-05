package com.dressca.applicationcore.domain.model;

public class Asset {
  private String assetCode;
  private String assetType;

  public Asset(String assetCode, String assetType) {
    this.assetCode = assetCode;
    this.assetType = assetType;
  }
  
  public String getAssetCode() {
    return assetCode;
  }
  public void setAssetCode(String assetCode) {
    this.assetCode = assetCode;
  }
  public String getAssetType() {
    return assetType;
  }
  public void setAssetType(String assetType) {
    this.assetType = assetType;
  }
}
