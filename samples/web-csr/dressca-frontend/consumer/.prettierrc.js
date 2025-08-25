import prettierConfigBase from '../.prettierrc.js'
/**
 * @see https://prettier.io/docs/configuration
 * @type {import("prettier").Config}
 */
const config = {
  ...prettierConfigBase,
  plugins: ['prettier-plugin-tailwindcss'],
  tailwindStylesheet: './src/assets/base.css',
}

export default config
