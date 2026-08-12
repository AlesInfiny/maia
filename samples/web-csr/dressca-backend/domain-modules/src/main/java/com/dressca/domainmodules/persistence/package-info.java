/**
 * 各コンテキストの永続化実装が共通で利用する基盤を提供するモジュールです。
 *
 * <p>境界づけられたコンテキストではなく、MyBatis に依存する技術的な基盤です。
 * 他モジュールへの依存は一切許可しません。</p>
 */
@ApplicationModule(displayName = "永続化基盤", type = ApplicationModule.Type.CLOSED,
    allowedDependencies = {})
package com.dressca.domainmodules.persistence;

import org.springframework.modulith.ApplicationModule;
