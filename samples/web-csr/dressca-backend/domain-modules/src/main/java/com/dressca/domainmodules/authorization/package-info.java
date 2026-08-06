/**
 * 認可に関する共通部品を提供するモジュールです。
 *
 * <p>内部を隠蔽しないオープンモジュールとして宣言し、他モジュールへの依存は一切許可しません。</p>
 */
@ApplicationModule(displayName = "認可部品", type = ApplicationModule.Type.OPEN,
    allowedDependencies = {})
package com.dressca.domainmodules.authorization;

import org.springframework.modulith.ApplicationModule;
