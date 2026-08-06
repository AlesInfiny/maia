/**
 * 認可に関する共通部品を提供するモジュールです。
 *
 * <p>内部を隠蔽しないオープンモジュールとして宣言し、他モジュールへの依存は一切許可しません。</p>
 */
@org.springframework.modulith.ApplicationModule(displayName = "認可部品",
    type = org.springframework.modulith.ApplicationModule.Type.OPEN, allowedDependencies = {})
package com.dressca.domainmodules.authorization;
