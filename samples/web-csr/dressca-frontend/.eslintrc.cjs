/* eslint-env node */
require('@rushstack/eslint-patch/modern-module-resolution');

module.exports = {
  root: true,
  parser: 'vue-eslint-parser', // これが必要
  extends: [
    'plugin:vue/vue3-recommended',
    'eslint:recommended',
    '@vue/eslint-config-airbnb-with-typescript',
    '@vue/eslint-config-prettier',
    "plugin:vue-data-testid/base"
  ],
  rules: {
    'import/prefer-default-export': 'off',
    'import/no-default-export': 'error',
  },
  overrides: [
    {
      files: [
        'cypress/e2e/**/*.{cy,spec}.{js,ts,jsx,tsx}',
        'cypress/support/**/*.{js,ts,jsx,tsx}',
      ],
      extends: ['plugin:cypress/recommended'],
    },
  ],
  ignorePatterns: ['postcss.config.cjs', 'tailwind.config.js'],
};
