package com.dressca.infrastructure.repository.jdbc;

import java.util.Optional;

import com.dressca.infrastructure.repository.jdbc.entity.AssetEntity;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface JdbcAssetRepository extends CrudRepository<AssetEntity, Long> {

  @Query("SELECT * FROM asset WHERE assetCode = :assetCode" )
  AssetEntity findByAssetCode(@Param("assetCode") String assetCode);
}
