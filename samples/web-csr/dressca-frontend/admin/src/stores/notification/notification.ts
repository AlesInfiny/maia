import { defineStore } from 'pinia'

/**
 * 通知のストアです。
 */
export const useNotificationStore = defineStore('notification', {
  state: () => ({
    message: '' as string,
    timeout: 5000 as number,
  }),
  actions: {
    /**
     * メッセージとタイムアウトまでの時間を設定します。
     * @param message メッセージ。
     * @param timeout タイムアウトまでの時間（ミリ秒）。
     */
    setMessage(message: string, timeout: number = 5000) {
      this.message = message
      this.timeout = timeout

      setTimeout(() => {
        this.clearMessage()
      }, this.timeout)
    },

    /**
     * メッセージを空にします。
     */
    clearMessage() {
      this.message = ''
    },
  },
})
