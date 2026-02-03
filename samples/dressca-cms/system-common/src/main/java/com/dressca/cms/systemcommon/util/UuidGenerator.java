package com.dressca.cms.systemcommon.util;

import java.util.UUID;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochGenerator;

/**
 * UUID v7 を生成するユーティリティクラスです。
 */
public class UuidGenerator {

  private static final TimeBasedEpochGenerator uuidGenerator = Generators.timeBasedEpochGenerator();

  /**
   * UUID v7 を生成します。
   * 
   * @return 生成された UUID v7。
   */
  public static UUID generate() {
    return uuidGenerator.generate();
  }
}
