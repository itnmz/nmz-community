import request from '@/utils/request'
import type { Result, LoginVO, LoginForm, RegisterForm } from '@/types/api'

/** 发送短信验证码（验证码会打印在后端控制台） */
export function sendSmsCode(phone: string) {
  return request.post<void>('/nmz/v1/auth/sms-code', { phone })
}

/** 注册：手机号 + 验证码 + 密码，成功直接返回 token */
export function register(data: RegisterForm): Promise<Result<LoginVO>> {
  return request.post<LoginVO>('/nmz/v1/auth/register', data)
}

/** 登录：手机号 + 密码 */
export function login(data: LoginForm): Promise<Result<LoginVO>> {
  return request.post<LoginVO>('/nmz/v1/auth/login', data)
}
