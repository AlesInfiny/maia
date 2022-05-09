package com.dressca.systemcommon.constant;

/**
 * 例外ID用定数クラス
 */
public class ExceptionIdConstant {
    /** 想定外のエラー */
    public static final String E_SHAR0000 = "E_SHAR0000";

    // 業務例外用
    /** アセットが見つからなかった際の例外 */
    public static final String E_ASSET0001 = "E_ASSET0001";

    /** 買い物かごが見つからなかった際の例外 */
    public static final String E_BASKET0001 = "E_BASKET0001";

    /** 注文のチェックアウト処理開始時に買い物かごが空だった際の例外 */
    public static final String E_ORDER0001 = "E_ORDER0001";

    /** 注文情報が存在しなかった際の例外 */
    public static final String E_ORDER0002 = "E_ORDER0002";
}
