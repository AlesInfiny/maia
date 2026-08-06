/**
 * カタログ管理コンテキストに関するモジュールです。
 *
 * <p>内部を隠蔽しないオープンモジュールとして宣言し、他モジュールへの依存は一切許可しません。</p>
 */
@ApplicationModule(displayName = "カタログ管理コンテキスト", type = ApplicationModule.Type.CLOSED,
    allowedDependencies = {"authorization", "common"})
package com.dressca.domainmodules.catalogmanagement;

import org.springframework.modulith.ApplicationModule;
