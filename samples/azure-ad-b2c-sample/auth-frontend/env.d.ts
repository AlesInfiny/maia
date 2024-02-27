/// <reference types="vite/client" />
interface ImportMetaEnv {
  readonly VITE_NO_ASSET_URL: string;
  readonly VITE_ASSET_URL: string;
  readonly VITE_BACKEND_ENDPOINT_ORIGIN: string;
  readonly VITE_API_PREFIX: string;
  readonly VITE_USER_FLOW_SIGN_IN: string;
  readonly VITE_ADB2C_AUTHORITY_DOMAIN: string;
  readonly VITE_ADB2C_TASKS_SCOPE: string;
  readonly VITE_ADB2C_APP_CLIENT_ID: string;
  readonly VITE_APP_URI: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
