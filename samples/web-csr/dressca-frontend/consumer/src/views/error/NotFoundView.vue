<template>
  <div class="bg-white text-gray-800">
    <div class="flex justify-center">
      <div class="bg-white p-8">
        <p class="mb-4 text-2xl font-medium">
          ご指定のページが見つかりませんでした。
        </p>
        <p class="mb-6 text-lg">
          アクセスしようとしたページは、削除・更新されたか、現在利用できない可能性があります。<br />
          トップページまたはメニューから、再度お探しください。
        </p>
        <div class="flex space-x-4 text-lg">
          <RouterLink to="/" class="text-blue-600 underline hover:text-blue-900"
            >トップページに戻る</RouterLink
          >
        </div>
      </div>
    </div>
  </div>
</template>
