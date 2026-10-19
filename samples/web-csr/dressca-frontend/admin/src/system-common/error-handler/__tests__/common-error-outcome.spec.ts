import { describe, expect, it } from 'vitest'
import { CanceledError } from 'axios'
import { useCommonErrorOutcome } from '@/system-common/error-handler/common-error-outcome'
import { ServerError, UnauthorizedError } from '@/system-common/error-handler/custom-error'

describe('共通のエラー処理に通した結果', () => {
  const toOutcome = useCommonErrorOutcome()

  it('サーバーエラー_failedを返す', async () => {
    // Arrange
    // Act
    const outcome = await toOutcome(new ServerError(''))
    // Assert
    expect(outcome.kind).toBe('failed')
  })

  it('認証エラー_ログイン画面へ誘導済みのunauthorizedを返す', async () => {
    // Arrange
    // Act
    const outcome = await toOutcome(new UnauthorizedError(''))
    // Assert
    expect(outcome).toEqual({ kind: 'unauthorized' })
  })

  it('通信の中断_canceledを返す', async () => {
    // Arrange
    // Act
    const outcome = await toOutcome(new CanceledError(''))
    // Assert
    expect(outcome).toEqual({ kind: 'canceled' })
  })
})
