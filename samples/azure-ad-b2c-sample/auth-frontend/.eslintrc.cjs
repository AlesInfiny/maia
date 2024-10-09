/* eslint-env node */
require('@rushstack/eslint-patch/modern-module-resolution');

module.exports = {
  root: true,
  extends: [
    'plugin:vue/vue3-recommended',
    'eslint:recommended',
    '@vue/eslint-config-airbnb-with-typescript',
    '@vue/eslint-config-prettier',
  ],
  rules: {
    'import/prefer-default-export': 'off',
    'import/no-default-export': 'error',
  },
  env: {
    'vue/setup-compiler-macros': true,
  },
};
