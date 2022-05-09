package com.dressca.systemcommon.constant;

/**
 * メッセージID用定数クラス
 */
public class MessageIdConstant {

    // システムエラーメッセージ用
    /** 想定外のエラーが発生しました */
    public static final String E_SHAR0000_FRONT = "F_SHAR0000.front";
    public static final String E_SHAR0000_LOG = "F_SHAR0000.log";

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
}
