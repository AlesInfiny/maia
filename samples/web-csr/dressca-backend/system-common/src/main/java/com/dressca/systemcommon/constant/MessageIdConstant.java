package com.dressca.systemcommon.constant;

/**
 * メッセージID用定数クラス。
 */
public class MessageIdConstant {

  // システムエラーメッセージ用
  /** 想定外のエラーが発生しました。 */
  public static final String E_SHARE0000_FRONT = "F_SHARE0000.front";
  public static final String E_SHARE0000_LOG = "F_SHARE0000.log";

  // 業務エラーメッセージ用
  /** アセットコード: {0} のアセットが見つかりませんでした。 */
  public static final String E_ASSET0001_FRONT = "E_ASSET0001.front";
  public static final String E_ASSET0001_LOG = "E_ASSET0001.log";

  /** 買い物かごID: {0} の買い物かごが見つかりませんでした。 */
  public static final String E_BASKET0001_FRONT = "E_BASKET0001.front";
  public static final String E_BASKET0001_LOG = "E_BASKET0001.log";

  /** 注文のチェックアウト時に買い物かごが空でした。 */
  public static final String E_ORDER0001_FRONT = "E_ORDER0001.front";
  public static final String E_ORDER0001_LOG = "E_ORDER0001.log";

  /** 注文ID: {0}, 購入者ID: {1} に該当する注文情報が見つかりませんでした。 */
  public static final String E_ORDER0002_FRONT = "E_ORDER0002.front";
  public static final String E_ORDER0002_LOG = "E_ORDER0002.log";

  /** アセット情報{0}を取得します。 */
  public static final String D_ASSET0001_LOG = "D_ASSET0001.log";

  /** 買い物かごに商品（顧客ID: {0}, カタログ商品ID: {1}, 数量: {2}）を追加します。 */
  public static final String D_BASKET0001_LOG = "D_BASKET0001.log";

  /** 買い物かごの商品の数量（顧客ID: {0}, 数量: {1}）を設定します。 */
  public static final String D_BASKET0002_LOG = "D_BASKET0002.log";

  /** 顧客（顧客ID: {0}）の買い物かごと情報とその商品一覧を取得します。 */
  public static final String D_BASKET0003_LOG = "D_BASKET0003.log";

  /** 注文（顧客ID: {0}, お届け先: {1}）を確定します。 */
  public static final String D_BASKET0004_LOG = "D_BASKET0004.log";

  /** 指定した注文ID: {0}, 購入者ID: {1} の注文情報を取得します。 */
  public static final String D_ORDER0001_LOG = "D_ORDER0001.log";

  /** 条件（ブランドID: {0}, カテゴリID: {1}, ページ: {2}, ページサイズ: {3}）に一致するカタログ情報を取得します。 */
  public static final String D_CATALOG0001_LOG = "D_CATALOG0001.log";

  /** 条件（ブランドID: {0}, カテゴリID: {1}）に一致するカテゴリの件数を取得します。 */
  public static final String D_CATALOG0002_LOG = "D_CATALOG0002.log";

  /** フィルタリング用のカタログブランドリストを取得します。 */
  public static final String D_CATALOG0003_LOG = "D_CATALOG0003.log";

  /** フィルタリング用のカタログカテゴリリストを取得します。 */
  public static final String D_CATALOG0004_LOG = "D_CATALOG0004.log";

  /** カタログアイテム ID: {0} のカタログアイテムを取得します。 */
  public static final String D_CATALOG0005_LOG = "D_CATALOG0005.log";

  /** カタログにアイテムを追加します。 */
  public static final String D_CATALOG0006_LOG = "D_CATALOG0006.log";

  /** カタログアイテム ID: {0} のカタログアイテムを削除します。 */
  public static final String D_CATALOG0007_LOG = "D_CATALOG0007.log";

  /** カタログアイテム ID: {0} のカタログアイテムを更新します。 */
  public static final String D_CATALOG0008_LOG = "D_CATALOG0008.log";
}
