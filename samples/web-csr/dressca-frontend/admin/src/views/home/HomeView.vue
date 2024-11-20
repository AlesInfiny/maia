<script setup lang="ts"></script>

<template>
  <div
    class="container mx-auto flex flex-col items-center justify-center gap-6"
  >
    <div class="p-8 text-5xl font-bold">Dressca 管理 トップ</div>
    <table class=",- table-auto border-separate">
      <thead>
        <tr>
          <th colspan="2" class="border bg-blue-800 p-4 text-3xl text-white">
            メニュー
          </th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td class="border bg-blue-50 p-8 text-3xl underline">
            <router-link to="/catalog/items">カタログアイテム管理</router-link>
          </td>
          <td class="border bg-blue-50 p-8 text-3xl">
            カタログアイテムを閲覧・追加・編集・削除します。
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
