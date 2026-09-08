/*
 * 各ドメインが定義したルート名定数を集約して再公開します。
 * ドメインをまたぐ画面遷移は、相手ドメインを直接参照せずこのモジュールを経由します。
 *
 * 参照方向のルール上、共通層からドメインへの参照は本来禁止ですが、
 * ルーティング定義は全ドメインを集約する役割を持つため、
 * アプリケーションシェル（App.vue / main.ts）と同じく例外として扱います。
 * 例外の範囲は本モジュールと index.ts に限定します。
 * system-common の他のコードから本モジュールを参照すると例外が層全体へ広がるため、
 * その参照は ESLint で禁止しています。
 */
import { authenticationRouteNames } from '@/authentication/router/authentication-route-names'
import { basketRouteNames } from '@/shopping/basket/router/basket-route-names'
import { displayItemRouteNames } from '@/shopping/display-item/router/display-item-route-names'
import { orderingRouteNames } from '@/shopping/ordering/router/ordering-route-names'
import { errorRouteNames } from './error-route-names'

export {
  authenticationRouteNames,
  basketRouteNames,
  displayItemRouteNames,
  orderingRouteNames,
  errorRouteNames,
}
