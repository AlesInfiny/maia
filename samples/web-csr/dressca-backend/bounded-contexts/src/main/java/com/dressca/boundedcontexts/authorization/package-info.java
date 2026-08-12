/**
 * 認可コンテキストに関するモジュールです。
 *
 * <p>他のコンテキストが {@code constant} および {@code exception} 配下の型を直接参照しているため、
 * 当面は内部を隠蔽しないオープンモジュールとして宣言します。
 * 公開する型を整理できた時点でクローズドモジュールへ移行します。</p>
 */
@ApplicationModule(displayName = "認可コンテキスト", type = ApplicationModule.Type.OPEN,
    allowedDependencies = {})
package com.dressca.boundedcontexts.authorization;

import org.springframework.modulith.ApplicationModule;
