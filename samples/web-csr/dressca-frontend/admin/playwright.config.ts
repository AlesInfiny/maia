import { defineConfig, devices } from '@playwright/test'

// プロキシを設定している環境では localhost へのアクセスがプロキシ経由となり、
// ローカル開発サーバーおよびバックエンドへの接続に失敗することがあります。
process.env.NO_PROXY = process.env.NO_PROXY
  ? `${process.env.NO_PROXY},localhost,127.0.0.1`
  : 'localhost,127.0.0.1'

// Gradle ラッパーの実行方法は OS によって異なります。
// Windows のコマンドプロンプトでは './gradlew' を解決できず、 Linux / macOS では
// PATH に 'gradlew' が存在しないため、いずれか一方の記法では両環境で動作しません。
const gradlew = process.platform === 'win32' ? 'gradlew.bat' : './gradlew'

export default defineConfig({
  testDir: './e2e',
  // consumer と admin のバックエンドは同一の H2 データベース（ localhost:9092 ）を
  // 共有するため、テストを並列実行するとデータの競合が発生します。
  fullyParallel: false,
  workers: 1,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 2 : 0,
  reporter: 'html',
  use: {
    baseURL: 'http://localhost:6173',
    locale: 'ja-JP',
    trace: 'on-first-retry',
  },
  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] },
    },
  ],
  webServer: [
    {
      command: 'npm run dev',
      url: 'http://localhost:6173',
      reuseExistingServer: !process.env.CI,
      timeout: 60 * 1000,
    },
    {
      command: `${gradlew} :web-admin:bootRunDev`,
      cwd: '../../dressca-backend',
      // データベースを含めた起動完了を確認するため、ヘルスチェック API を使用します。
      url: 'http://localhost:8081/api/health/datasource',
      reuseExistingServer: !process.env.CI,
      // Gradle デーモンの起動とコンパイルを含むため、長めに設定します。
      timeout: 300 * 1000,
    },
  ],
})
