package com.dressca.systemcommon.constant;

/**
 * 例外ID用定数クラス。
 */
public class ExceptionIdConstant {
  /** 想定外のエラー。 */
  public static final String E_SHARE0000 = "E_SHARE0000";

  // 業務例外用
  /** アセットが見つからなかった際の例外。 */
  public static final String E_ASSET0001 = "E_ASSET0001";

  /** 買い物かごが見つからなかった際の例外。 */
  public static final String E_BASKET0001 = "E_BASKET0001";

  /** 買い物かご内に想定した注文情報が存在しなかった際の例外。 */
  public static final String E_BASKET0002 = "E_BASKET0002";

  /** 注文のチェックアウト処理開始時に買い物かごが空だった際の例外。 */
  public static final String E_ORDER0001 = "E_ORDER0001";

  /** 注文情報が存在しなかった際の例外。 */
  public static final String E_ORDER0002 = "E_ORDER0002";

  /** カタログ情報が存在しなかった際の例外。 */
  public static final String E_CATALOG0001 = "E_CATALOG0001";

  /** カタログブランドが存在しなかった際の例外。 */
  public static final String E_CATALOG0002 = "E_CATALOG0002";

  /** カタログカテゴリが存在しなかった際の例外。 */
  public static final String E_CATALOG0003 = "E_CATALOG0003";
}
