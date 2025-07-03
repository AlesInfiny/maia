// TODO Maia・Maris 間でProblemDetails が共通化できたら、@/generated/api-client/model から import します。
import type { ProblemDetails } from '@/shared/error-handler/custom-error'
import { AxiosError, AxiosHeaders } from 'axios'

/**
 * 引数の ProblemDetails オブジェクトの情報を持つ AxiosError を作成します。
 *
 * @param {ProblemDetails} problemDetails - API レスポンスから取得した詳細な問題情報。
 * @returns {AxiosError} 作成した AxiosError 。
 *
 */
export function createAxiosError(problemDetails: ProblemDetails): AxiosError {
  const error = new AxiosError('', '', undefined, null, {
    status: problemDetails.status,
    statusText: '',
    headers: {},
    config: {
      headers: new AxiosHeaders({
        'Content-Type': 'application/json',
      }),
    },
    data: {
      detail: problemDetails.detail,
      exceptionId: problemDetails.exceptionId,
      exceptionValues: problemDetails.exceptionValues,
      instance: problemDetails.instance,
      status: problemDetails.status,
      title: problemDetails.title,
      type: problemDetails.type,
    },
  })
  return error
}

/**
 * 引数の情報を持つ、 API のエラーレスポンスの詳細な問題情報（`ProblemDetails`）を作成します。
 * 引数の各プロパティが指定されていない場合は、デフォルト値を使用します。
 *
 * @param {Partial<ProblemDetails>} param - `ProblemDetails`のプロパティの一部を含むオブジェクト。
 * @param {string} [param.detail='No details available'] - 問題の詳細。
 * @param {string} [param.exceptionId='exceptionId'] - 例外の ID 。
 * @param {Array<string>} [param.exceptionValues=[]] - 例外に関連する値の配列。
 * @param {string} [param.instance='/api/'] - 問題が発生したリソースの URI 。
 * @param {number} [param.status=500] - HTTP ステータスコード。
 * @param {string} [param.title='Internal Server Error'] - タイトル。
 * @param {string} [param.type='about:blank'] - 問題の種類を識別する URI 。
 * @returns {ProblemDetails} 作成した`ProblemDetails`。
 *
 */
export function createProblemDetails({
  detail = 'No details available',
  exceptionId = 'exceptionId',
  exceptionValues = [],
  instance = '/api/',
  status = 500,
  title = 'Internal Server Error',
  type = 'about:blank',
}: Partial<ProblemDetails>): ProblemDetails {
  return {
    detail,
    exceptionId,
    exceptionValues,
    instance,
    status,
    title,
    type,
  }
}
