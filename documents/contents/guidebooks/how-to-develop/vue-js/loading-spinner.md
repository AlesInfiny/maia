---
title: Vue.js 開発手順
description: Vue.js を用いた クライアントサイドアプリケーションの 開発手順を説明します。
---

# ローディングスピナーの実装 {#top}

ローディングスピナーの実装方法を解説します。

ローディングスピナーの実装には次の利点があります。

- API のレスポンスを待機している間の崩れたレイアウトをユーザーから隠蔽できる。
- ユーザーにデータの送受信中であることを明示できる。

実装手順は以下の通りです。

1. ローディングスピナーのコンポーネントを作成する。
1. vue ファイルで作成したコンポーネントを利用して表示・非表示を制御する。

初めに、ローディングスピナーのコンポーネントを作成します。

以下は Tailwind CSS を用いた実装例です。

??? example "ローディングスピナーのコンポーネントの実装例"

    ``` ts
    <script setup lang="ts">
    /**
     * API からデータが返ってくるまで画面に表示されるローディングスピナーです。
     */
    defineProps<{
      show: boolean;
    }>();
    </script>

    <template>
      <div
        v-if="show"
        class="fixed top-0 left-0 right-0 bottom-0 w-full h-screen z-50 overflow-hidden bg-transparent flex items-center justify-center"
      >
        <div class="flex items-center justify-center">
          <div class="flex justify-center">
            <div
              class="animate-spin h-14 w-14 border-4 rounded-full border-gray-400 border-t-gray-200"
            ></div>
          </div>
        </div>
      </div>
    </template>
    ```

次に、 ルーティングが指定されている vue ファイルで作成したコンポーネントを使用して、コンテンツの表示・非表示を制御します。

表示・非表示の制御には ref オブジェクトの真理値を利用します。
コンポーネントがマウントされた時のライフサイクルフックである [onMounted :material-open-in-new:](https://ja.vuejs.org/api/composition-api-lifecycle#onmounted){target=_blank} を使って ref オブジェクトの真理値を切り替えます。
API 呼び出しの際に例外が発生する可能性のある場合は、 finally 句でスピナーを非表示にする処理を追加してください。
最後に、 template タグ内で ref オブジェクトの真理値を使って表示するコンテンツを切り替えます。

以下は、ローディングスピナーのコンポーネントが実装された vue ファイルの例です。

??? example "ローディングスピナーのコンポーネントが実装された vue ファイルの例"

    ``` ts
    <script setup lang="ts">
    import { onMounted, ref } from 'vue';
    import LoadingSpinner from '@/components/LoadingSpinner.vue';

    /**
     * ローディングスピナーの表示の状態です。
     */
    const showLoading = ref(true);

    onMounted(async () => {
      showLoading.value = true;
      try {
        // API を呼び出してデータを取得する処理
      } catch (error) {
        // API を呼び出した時の例外処理
      } finally {
        showLoading.value = false;
      }
    });
    </script>

    <template>
      <LoadingSpinner :show="showLoading"></LoadingSpinner>
      <div v-if="!showLoading">
      // メインのコンテンツ
      </div>
    </template>
    ```
