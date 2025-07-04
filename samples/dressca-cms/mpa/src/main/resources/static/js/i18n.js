    (async () => {
      // 1. JSON の置き場所（Thymeleaf で解決）
      const basePath = '/templates/greeting/';
      // 2. ブラウザ言語を先頭2文字で取得（例: "ja" or "en"）
      const lng = (navigator.language || 'en').slice(0,2);

      try {
        // 3. JSON を取得
        const res  = await fetch(`${basePath}${lng}.json`);
        const data = await res.json();

        // 4. i18next を初期化。プラグイン不要で resources に直接渡す
        await i18next.init({
          lng,
          fallbackLng: 'en',
          debug: false,
          resources: {
            [lng]: { translation: data }
          },
        });

        // 5. ページ上のすべての data-i18n を走査して差し替え
        translatePage();

      } catch (e) {
        console.error('翻訳ファイルの読み込み／初期化に失敗:', e);
      }
    })();

    // 翻訳を当て込む関数
    function translatePage() {
      document.querySelectorAll('[data-i18n]').forEach(el => {
        const key = el.getAttribute('data-i18n');
        // title 要素の場合は document.title も更新
        if (el.tagName === 'TITLE') {
          document.title = i18next.t(key);
        } else {
          el.textContent = i18next.t(key);
        }
      });
    }