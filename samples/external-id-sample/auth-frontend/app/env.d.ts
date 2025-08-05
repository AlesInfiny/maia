/// <reference types="vite/client" />
interface ImportMetaEnv {
  readonly VITE_NO_ASSET_URL: string
  readonly VITE_ASSET_URL: string
  readonly VITE_AXIOS_BASE_ENDPOINT_ORIGIN: string
  readonly VITE_PROXY_ENDPOINT_ORIGIN: string
  readonly VITE_EXTERNAL_ID_AUTHORITY_DOMAIN: string
  readonly VITE_EXTERNAL_ID_SCOPE: string
  readonly VITE_EXTERNAL_ID_APP_CLIENT_ID: string
  readonly VITE_EXTERNAL_ID_APP_URI: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}
