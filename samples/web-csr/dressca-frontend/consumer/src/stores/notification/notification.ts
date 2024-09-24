import { defineStore } from 'pinia';

export const useNotificationStore = defineStore({
  id: 'notification',
  state: () => ({
    message: '' as string,
    timeout: 5000 as number,
  }),
  actions: {
    setMessage(message: string, timeout: number = 5000) {
      this.message = message;
      this.timeout = timeout;

      setTimeout(() => {
        this.clearMessage();
      }, this.timeout);
    },

    clearMessage() {
      this.message = '';
    },
  },
});
