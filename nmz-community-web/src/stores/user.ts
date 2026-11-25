import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import type { LoginVO, UserVO } from '@/types/api'

const TOKEN_KEY = 'token'
const USER_INFO_KEY = 'userInfo'

function readUserInfo(): UserVO | null {
  const raw = localStorage.getItem(USER_INFO_KEY)
  if (!raw) return null
  try {
    return JSON.parse(raw) as UserVO
  } catch {
    localStorage.removeItem(USER_INFO_KEY)
    return null
  }
}

/**
 * 当前登录用户状态。
 *
 * token 与 userInfo 同步落到 localStorage，刷新页面后仍保持登录。
 */
export const useUserStore = defineStore('user', () => {
  const token = ref<string>(localStorage.getItem(TOKEN_KEY) ?? '')
  const userInfo = ref<UserVO | null>(readUserInfo())

  const isLogin = computed(() => Boolean(token.value))
  const nickname = computed(() => userInfo.value?.nickname ?? '')

  /** 登录 / 注册成功后写入 */
  function setLogin(vo: LoginVO) {
    token.value = vo.token
    userInfo.value = vo.userInfo
    localStorage.setItem(TOKEN_KEY, vo.token)
    localStorage.setItem(USER_INFO_KEY, JSON.stringify(vo.userInfo))
  }

  /** 退出登录 */
  function logout() {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(USER_INFO_KEY)
  }

  return { token, userInfo, isLogin, nickname, setLogin, logout }
})
