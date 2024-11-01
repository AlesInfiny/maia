// 日英対応などが必要な場合は、設定を追加する。

export function languageHelper() {
  const toConfigureLocale = () => {
    const browserLanguage = window.navigator.language;
    let language = 'ja';
    if (browserLanguage !== 'ja' && browserLanguage !== 'en') {
      language = 'en';
    } else {
      language = browserLanguage;
    }
    return language;
  };
  return {
    toConfigureLocale,
  };
}
