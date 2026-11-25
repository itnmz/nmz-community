import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import './styles/index.scss'

const app = createApp(App)

// Pinia 必须在 router 之前安装：路由守卫里会用到 store
app.use(createPinia())
app.use(router)

app.mount('#app')
