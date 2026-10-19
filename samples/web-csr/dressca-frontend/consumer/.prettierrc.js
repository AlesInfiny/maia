import prettierConfigBase from '../.prettierrc.js'
/**
 * @see https://prettier.io/docs/configuration
 * @type {import("prettier").Config}
 */
const config = {
  ...prettierConfigBase,
  plugins: ['prettier-plugin-tailwindcss'],
  tailwindStylesheet: './src/core/styles/base.css',
}

export default config
