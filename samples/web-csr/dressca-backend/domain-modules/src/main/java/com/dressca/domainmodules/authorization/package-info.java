/**
 * 認可コンテキストに関するモジュールです。
 *
 * <p>内部を隠蔽しないオープンモジュールとして宣言し、共通部品以外への依存は許可しません。</p>
 */
@ApplicationModule(displayName = "認可部品", type = ApplicationModule.Type.OPEN,
    allowedDependencies = {})
package com.dressca.domainmodules.authorization;

import org.springframework.modulith.ApplicationModule;
