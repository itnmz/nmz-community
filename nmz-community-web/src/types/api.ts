/** 后端统一返回体，与 Java 端的 Result<T> 一一对应 */
export interface Result<T = unknown> {
  code: number
  message: string
  data: T
}

/** 后端 UserVO，注意不含 password 字段 */
export interface UserVO {
  id: number
  phone: string
  nickname: string
  avatar: string
  role: string
}

/** 登录 / 注册成功后的返回体 */
export interface LoginVO {
  token: string
  userInfo: UserVO
}

/** 登录入参 */
export interface LoginForm {
  phone: string
  password: string
}

/** 注册入参，nickname 选填 */
export interface RegisterForm {
  phone: string
  code: string
  password: string
  nickname?: string
}
