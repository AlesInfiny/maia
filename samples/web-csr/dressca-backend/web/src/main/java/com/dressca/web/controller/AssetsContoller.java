package com.dressca.web.controller;

import com.dressca.applicationcore.domain.model.Asset;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assets")
public class AssetsContoller {

  /**
   * アセットを取得します。
   * 
   * @param assetCode アセットコード
   * @return アセット
   */
  @GetMapping("{assetCode}")
  public ResponseEntity<Asset> get(@PathVariable String assetCode) {

    if (assetCode.equals("notExist")) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
    }

    return new ResponseEntity<>(new Asset(assetCode, "assetType:" + assetCode), HttpStatus.OK); 
  }
}
