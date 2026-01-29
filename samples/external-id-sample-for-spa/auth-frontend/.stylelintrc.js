export default {
  extends: ['stylelint-config-standard', 'stylelint-config-recommended-vue'],
  ignoreFiles: ['dist/**/*', 'coverage/**/*'],
  overrides: [
    {
      files: ['**/*.vue'],
      /** Vue ファイルの <style> ブロック内を Lint するための設定です。*/
      customSyntax: 'postcss-html',
    },
  ],
}
