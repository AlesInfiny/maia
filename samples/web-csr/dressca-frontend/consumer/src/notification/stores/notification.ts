import { defineStore } from 'pinia'

/**
 * 通知メッセージを管理するストアです。
 *
 * エラーメッセージや成功通知などのメッセージ内容・タイトル・詳細を保持し、
 * 一定時間後に自動的にクリアします。
 */
export const useNotificationStore = defineStore('notification', {
  state: () => ({
    message: '' as string,
    id: '' as string,
    title: '' as string,
    detail: '' as string,
    status: 0 as number,
    timeout: 5000 as number,
  }),
  actions: {
    /**
     * 通知メッセージを設定します。
     * 一定時間後（`timeout`）に自動でクリアされます。
     * @param message 通知本文。
     * @param id 通知の識別子。
     * @param title 通知タイトル。
     * @param detail 詳細メッセージ。
     * @param status 通知ステータス。
     * @param timeout 自動クリアまでの時間（ミリ秒）。
     */
    setMessage(
      message: string,
      id: string,
      title: string,
      detail: string,
      status: number = 0,
      timeout: number = 5000,
    ) {
      this.message = message
      this.id = id
      this.title = title
      this.detail = detail
      this.status = status
      this.timeout = timeout

      setTimeout(() => {
        this.clearMessage()
      }, this.timeout)
    },
    /**
     * 通知メッセージをクリアします。
     *
     * すべてのフィールド（message, id, title, detail, status）を初期化します。
     */
    clearMessage() {
      this.message = ''
      this.id = ''
      this.title = ''
      this.detail = ''
      this.status = 0
    },
  },
})
