/**
 * カタログ管理コンテキストに関するモジュールです。
 */
@ApplicationModule(displayName = "カタログ管理コンテキスト", type = ApplicationModule.Type.CLOSED,
    allowedDependencies = {"authorization", "common"})
package com.dressca.domainmodules.catalogmanagement;

import org.springframework.modulith.ApplicationModule;
