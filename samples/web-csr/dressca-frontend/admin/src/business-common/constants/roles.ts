/**
 * ユーザーのロールを表す文字列列挙型です。
 *
 * ロールはバックエンドの API に由来する業務概念であり、認可判定を行う
 * 複数のドメインから参照されるため business-common に配置します。
 */
export enum Roles {
  ADMIN = 'ROLE_ADMIN',
}
