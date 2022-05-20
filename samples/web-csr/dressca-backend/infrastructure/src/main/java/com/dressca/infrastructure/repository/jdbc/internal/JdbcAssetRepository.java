package com.dressca.infrastructure.repository.jdbc.internal;


import java.util.Optional;
import com.dressca.infrastructure.repository.jdbc.internal.entity.AssetEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface JdbcAssetRepository extends CrudRepository<AssetEntity, Long> {

  @Query("SELECT * FROM ASSETS WHERE ASSET_CODE = :assetCode")
  Optional<AssetEntity> findByAssetCode(@Param("assetCode") String assetCode);
}
