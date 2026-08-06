/**
 * 認可に関する共通部品を提供するモジュールです。
 *
 * <p>内部を隠蔽しないオープンモジュールとして宣言し、共通部品以外への依存は許可しません。</p>
 */
@ApplicationModule(displayName = "認可部品", type = ApplicationModule.Type.OPEN,
    allowedDependencies = {"common"})
package com.dressca.domainmodules.authorization;

import org.springframework.modulith.ApplicationModule;
