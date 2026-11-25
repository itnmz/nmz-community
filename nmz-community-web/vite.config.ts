import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

export default defineConfig({
  plugins: [
    vue(),
    // 自动导入 Vue / Vue Router / Pinia 的 API（ref、computed、useRouter 等）
    AutoImport({
      imports: ['vue', 'vue-router', 'pinia'],
      resolvers: [ElementPlusResolver()],
      dts: 'src/types/auto-imports.d.ts'
    }),
    // Element Plus 组件按需引入，避免整包打进产物
    Components({
      resolvers: [ElementPlusResolver()],
      dts: 'src/types/components.d.ts'
    })
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    // 显式绑定 IPv4：Vite 默认只监听 ::1，用 127.0.0.1 访问会连不上
    host: '127.0.0.1',
    port: 5173,
    // 后端在 8080，前端统一写相对路径 /nmz/...，由这里转发，浏览器视为同源，不触发跨域
    proxy: {
      '/nmz': {
        target: 'http://127.0.0.1:8080',
        changeOrigin: true
      }
    }
  }
})
