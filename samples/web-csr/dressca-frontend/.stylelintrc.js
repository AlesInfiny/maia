export default {
  extends: ['stylelint-config-standard', 'stylelint-config-recommended-vue'],
  rules: {
    'at-rule-no-unknown': [
      true,
      {
        ignoreAtRules: [
          /**
           * Tailwind CSS 固有のアットルール（ディレクティブ）をホワイトリストに登録します。
           * https://tailwindcss.com/docs/functions-and-directives#directives
           **/
          'theme',
          'source',
          'utility',
          'variant',
          'custom-variant',
          'apply',
          'reference',
        ],
      },
    ],
    'function-no-unknown': [
      true,
      {
        /**
         * Tailwind CSS が提供する関数をホワイトリストに登録します。
         * https://tailwindcss.com/docs/functions-and-directives#functions
         **/
        ignoreFunctions: ['alpha', 'spacing'],
      },
    ],
  },
  ignoreFiles: ['dist/**/*', 'coverage/**/*'],
  overrides: [
    {
      files: ['**/*.vue'],
      /** Vue ファイルの <style> ブロック内を Lint するための設定です。*/
      customSyntax: 'postcss-html',
    },
  ],
}
