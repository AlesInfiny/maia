package com.dressca.systemcommon.util;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochGenerator;
import java.util.UUID;

/**
 * UUID v7 を生成するユーティリティクラスです。
 */
public class UuidGenerator {

  private static final TimeBasedEpochGenerator UUID_GENERATOR =
      Generators.timeBasedEpochGenerator();

  /**
   * UUID v7 を生成します。
   *
   * @return 生成された UUID v7。
   */
  public static UUID generate() {
    return UUID_GENERATOR.generate();
  }
}
