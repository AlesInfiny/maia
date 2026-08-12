/**
 * 複数のコンテキストが共通で利用する部品を提供するモジュールです。
 */
@ApplicationModule(displayName = "コンテキスト共通部品", type = ApplicationModule.Type.OPEN,
    allowedDependencies = {})
package com.dressca.boundedcontexts.shared;

import org.springframework.modulith.ApplicationModule;
