<template>
  <div class="bg-white text-gray-800">
    <div class="flex justify-center">
      <div class="bg-white p-8">
        <p class="mb-4 text-2xl font-medium">指定したページが見つかりませんでした。</p>
        <p class="mb-6 text-lg">
          アクセスしようとしたページは、削除または移動されたか、現在利用できない状態です。<br />
          必要な情報が見つからない場合は、管理者にご連絡いただくか、システム内のメニューから再度お試しください。
        </p>
        <div class="flex gap-4 text-lg">
          <RouterLink to="/" class="text-blue-600 underline hover:text-blue-900">
            トップページに戻る
          </RouterLink>
        </div>
      </div>
    </div>
  </div>
</template>
