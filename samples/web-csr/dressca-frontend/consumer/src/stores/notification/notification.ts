import { defineStore } from 'pinia';

export const useNotificationStore = defineStore({
  id: 'notification',
  state: () => ({
    message: '' as string,
    id: '' as string,
    title: '' as string,
    detail: '' as string,
    timeout: 5000 as number,
  }),
  actions: {
    setMessage(
      message: string,
      id: string,
      title: string,
      detail: string,
      timeout: number = 5000,
    ) {
      this.message = message;
      this.id = id;
      this.title = title;
      this.detail = detail;
      this.timeout = timeout;

      setTimeout(() => {
        this.clearMessage();
      }, this.timeout);
    },

    clearMessage() {
      this.message = '';
      this.id = '';
      this.title = '';
      this.detail = '';
    },
  },
});
