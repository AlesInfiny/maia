import colors from 'vuetify/es5/util/colors'
const {
  NODE_ENV,
  USER_FLOW_SIGNIN,
  USER_FLOW_EDIT_PROFILE,
  ADB2C_SIGNIN_URI,
  ADB2C_EDIT_PROFILE_URI,
  ADB2C_AUTHORITY_DOMAIN,
  ADB2C_TASKS_SCOPE,
  API_GET_MESSAGE_URI,
  API_DELETE_USER_URI,
  ADB2C_APP_CLIENT_ID,
  APP_URI,
} = process.env

export default {
  dev: NODE_ENV !== 'production',
  // Disable server-side rendering: https://go.nuxtjs.dev/ssr-mode
  ssr: false,

  // Target: https://go.nuxtjs.dev/config-target
  target: 'static',

  // Global page headers: https://go.nuxtjs.dev/config-head
  head: {
    titleTemplate: '%s - sampledemo',
    title: 'sampledemo',
    htmlAttrs: {
      lang: 'en',
    },
    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      { hid: 'description', name: 'description', content: '' },
      { name: 'format-detection', content: 'telephone=no' },
    ],
    // ↓ファビコンの設定（必要ならコメントアウトを外す）
    // link: [{ rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }],
  },

  // Global CSS: https://go.nuxtjs.dev/config-css
  css: ['~/assets/css/main.css'],

  // Plugins to run before rendering page: https://go.nuxtjs.dev/config-plugins
  plugins: ['@/plugins/PiniaPersistPlugin'],

  // Auto import components: https://go.nuxtjs.dev/config-components
  components: true,

  // Modules for dev and build (recommended): https://go.nuxtjs.dev/config-modules
  buildModules: [
    // https://go.nuxtjs.dev/typescript
    '@nuxt/typescript-build',
    // https://go.nuxtjs.dev/stylelint
    '@nuxtjs/stylelint-module',
    // https://go.nuxtjs.dev/vuetify
    '@nuxtjs/vuetify',
    // https://composition-api.nuxtjs.org/
    '@nuxtjs/composition-api/module',
    ['@pinia/nuxt', { disableVuex: true }],
    '@nuxtjs/date-fns',
  ],

  router: {
    // mode: 'hash',
    middleware: ['redirect'],
  },

  // Modules: https://go.nuxtjs.dev/config-modules
  modules: [
    // https://go.nuxtjs.dev/axios
    '@nuxtjs/axios',
  ],

  // Axios module configuration: https://go.nuxtjs.dev/config-axios
  axios: {
    // Workaround to avoid enforcing hard-coded localhost:3000: https://github.com/nuxt-community/axios-module/issues/308
    baseURL: '/',
  },

  // Vuetify module configuration: https://go.nuxtjs.dev/config-vuetify
  vuetify: {
    customVariables: ['~/assets/variables.scss'],
    theme: {
      dark: false,
      themes: {
        light: {
          primary: colors.teal.lighten2,
          accent: colors.grey.darken3,
          secondary: colors.amber.darken3,
          info: colors.teal.lighten1,
          warning: colors.amber.base,
          error: colors.deepOrange.accent4,
          success: colors.green.accent3,
        },
      },
    },
  },
  env: {
    USER_FLOW_SIGNIN,
    USER_FLOW_EDIT_PROFILE,
    ADB2C_SIGNIN_URI,
    ADB2C_EDIT_PROFILE_URI,
    ADB2C_AUTHORITY_DOMAIN,
    ADB2C_TASKS_SCOPE,
    API_GET_MESSAGE_URI,
    API_DELETE_USER_URI,
    ADB2C_APP_CLIENT_ID,
    APP_URI,
  },

  // Build Configuration: https://go.nuxtjs.dev/config-build
  build: {},
}
