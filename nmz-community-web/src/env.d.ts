/// <reference types="vite/client" />

interface ImportMetaEnv {
  /** 接口基础地址，开发环境为 /（走 Vite 代理） */
  readonly VITE_API_BASE_URL: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}

declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}
