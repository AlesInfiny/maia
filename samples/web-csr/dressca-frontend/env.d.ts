/// <reference types="vite/client" />
interface ImportMetaEnv {
  readonly VITE_NO_ASSET_URL: string;
  readonly VITE_ASSET_URL: string;
  readonly VITE_BACKEND_ENDPOINT_ORIGIN: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
