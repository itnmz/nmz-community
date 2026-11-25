import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', redirect: '/home' },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/auth/LoginView.vue'),
      meta: { title: '登录' }
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/auth/RegisterView.vue'),
      meta: { title: '注册' }
    },
    {
      path: '/home',
      name: 'home',
      component: () => import('@/views/home/HomeView.vue'),
      meta: { title: '首页', requiresAuth: true }
    }
  ]
})

router.beforeEach((to) => {
  const userStore = useUserStore()

  // 未登录访问受保护页面 → 去登录页，并记住原目标
  if (to.meta.requiresAuth && !userStore.isLogin) {
    return { name: 'login', query: { redirect: to.fullPath } }
  }

  // 已登录还去登录/注册页 → 回首页
  if ((to.name === 'login' || to.name === 'register') && userStore.isLogin) {
    return { name: 'home' }
  }

  return true
})

router.afterEach((to) => {
  const title = to.meta.title as string | undefined
  document.title = title ? `${title} - nmz 社区` : 'nmz 社区'
})

export default router
