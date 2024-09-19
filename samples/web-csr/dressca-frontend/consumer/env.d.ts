/// <reference types="vite/client" />
interface ImportMetaEnv {
  readonly VITE_NO_ASSET_URL: string;
  readonly VITE_ASSET_URL: string;
  readonly VITE_AXIOS_BASE_ENDPOINT_ORIGIN: string;
  readonly VITE_PROXY_ENDPOINT_ORIGIN: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
