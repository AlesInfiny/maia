import { z } from 'zod'

/**
 * 前後の空白を除いて 1 文字以上であることを検証するルールを返します。
 *
 * 業務概念に依存しない汎用の入力チェックのため system-common に配置し、
 * 各コンテキストのバリデーション定義から参照します。
 * @param message 検証に失敗したときのメッセージ。
 * @returns 必須入力を検証する Zod スキーマ。
 */
export const required = (message: string) => z.string().trim().min(1, message)
