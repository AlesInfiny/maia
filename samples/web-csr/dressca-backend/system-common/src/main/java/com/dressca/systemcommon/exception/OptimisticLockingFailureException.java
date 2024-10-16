package com.dressca.systemcommon.exception;

/**
 * 楽観ロック違反をしたことを表す例外です。
 */
public class OptimisticLockingFailureException extends LogicException {

  /**
   * カタログアイテムidを指定して、
   * {@link OptimisticLockingFailureException} クラスの新しいインスタンスを初期化します。
   * 
   * @param catalogItemId 更新処理を試みたカタログアイテムid
   */
  public OptimisticLockingFailureException(long catalogItemId) {
    super(
        null,
        "カタログアイテム ID: {0} のカタログアイテムの更新時に楽観ロックエラーが発生しました。 ",
        new String[] { String.valueOf(catalogItemId) },
        new String[] { String.valueOf(catalogItemId) });
  }

}
