package com.dressca.infrastructure.repository;

import java.util.Optional;

import com.dressca.applicationcore.assets.Asset;
import com.dressca.applicationcore.assets.AssetRepository;
import com.dressca.infrastructure.repository.jdbc.JdbcAssetRepository;
import com.dressca.infrastructure.repository.jdbc.entity.AssetEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AssetRepositoryImpl implements AssetRepository {

  @Autowired
  private final JdbcAssetRepository jdbcAssetRepository;

  @Override
  public Optional<Asset> findByAssetCode(String assetCode) {
    AssetEntity entity = jdbcAssetRepository.findByAssetCode(assetCode);
    return Optional.of(new Asset(entity.getAssetCode(), entity.getAssetType()));
  }

}
