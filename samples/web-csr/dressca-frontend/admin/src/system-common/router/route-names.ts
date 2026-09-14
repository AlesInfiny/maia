/*
 * 各コンテキスト（およびその配下のドメイン）が定義したルート名定数を集約して再公開します。
 * コンテキスト・ドメインをまたぐ画面遷移は、相手を直接参照せずこのモジュールを経由します。
 *
 * 参照方向のルール上、共通層からコンテキストへの参照は本来禁止ですが、
 * ルーティング定義は全コンテキストを集約する役割を持つため、例外として扱います。
 * 例外の範囲は本モジュールと index.ts に限定します。
 */
import { authenticationRouteNames } from '@/authentication/login/router/authentication-route-names'
import { catalogRouteNames } from '@/catalog-management/catalog/router/catalog-route-names'
import { errorRouteNames } from './error-route-names'
import { homeRouteNames } from './home-route-names'

export { authenticationRouteNames, catalogRouteNames, errorRouteNames, homeRouteNames }
