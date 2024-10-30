import type { App, ComponentPublicInstance } from 'vue';

export const globalErrorHandler = {
  install(app: App) {
    /* eslint no-param-reassign: 0 */
    app.config.errorHandler = (
      err: unknown,
      instance: ComponentPublicInstance | null,
      info: string,
    ) => {
      /* eslint no-console: 0 */
      console.log(err, instance, info);
    };

    window.addEventListener('error', (event) => {
      /* eslint no-console: 0 */
      console.log(event);
    });

    window.addEventListener('unhandledrejection', (event) => {
      /* eslint no-console: 0 */
      console.log(event);
    });
  },
};
