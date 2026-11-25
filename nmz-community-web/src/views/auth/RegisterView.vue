<script setup lang="ts">
import { onUnmounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { Iphone, Lock, Message, User } from '@element-plus/icons-vue'
import 'element-plus/es/components/message/style/css'
import AuthLayout from '@/layouts/AuthLayout.vue'
import { register, sendSmsCode } from '@/api/auth'
import { useUserStore } from '@/stores/user'
import type { RegisterForm } from '@/types/api'

const router = useRouter()
const userStore = useUserStore()

const formRef = ref<FormInstance>()
const loading = ref(false)
const sending = ref(false)
const countdown = ref(0)
let timer: number | undefined

const form = reactive<RegisterForm>({
  phone: '',
  code: '',
  password: '',
  nickname: ''
})

const rules: FormRules = {
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { pattern: /^\d{6}$/, message: '验证码为 6 位数字', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度需为 6~20 位', trigger: 'blur' }
  ],
  nickname: [{ max: 20, message: '昵称最多 20 个字符', trigger: 'blur' }]
}

function stopCountdown() {
  if (timer !== undefined) {
    window.clearInterval(timer)
    timer = undefined
  }
  countdown.value = 0
}

function startCountdown() {
  countdown.value = 60
  timer = window.setInterval(() => {
    countdown.value -= 1
    if (countdown.value <= 0) stopCountdown()
  }, 1000)
}

onUnmounted(stopCountdown)

async function handleSendCode() {
  if (!/^1[3-9]\d{9}$/.test(form.phone)) {
    ElMessage.warning('请先输入正确的手机号')
    return
  }
  sending.value = true
  try {
    await sendSmsCode(form.phone)
    ElMessage.success('验证码已发送，请查看后端控制台')
    startCountdown()
  } catch {
    // 错误提示由拦截器统一处理
  } finally {
    sending.value = false
  }
}

async function handleSubmit() {
  if (!formRef.value) return
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const res = await register({
      phone: form.phone,
      code: form.code,
      password: form.password,
      nickname: form.nickname || undefined
    })
    userStore.setLogin(res.data)
    ElMessage.success('注册成功，已自动登录')
    router.replace({ name: 'home' })
  } catch {
    // 错误提示由拦截器统一处理
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <AuthLayout
    footer-text="已经有账号了？"
    footer-link-text="返回登录"
    footer-to="/login"
  >
    <h2 class="form-title">创建账号</h2>
    <p class="form-subtitle">验证码会打印在后端控制台，注册成功后自动登录。</p>

    <el-form
      ref="formRef"
      class="auth-form"
      :model="form"
      :rules="rules"
      @keyup.enter="handleSubmit"
    >
      <el-form-item prop="phone">
        <el-input
          v-model="form.phone"
          :prefix-icon="Iphone"
          placeholder="请输入手机号"
          maxlength="11"
          autocomplete="username"
        />
      </el-form-item>

      <el-form-item prop="code">
        <div class="code-row">
          <el-input
            v-model="form.code"
            :prefix-icon="Message"
            placeholder="6 位验证码"
            maxlength="6"
          />
          <el-button
            class="code-btn"
            :disabled="countdown > 0"
            :loading="sending"
            @click="handleSendCode"
          >
            {{ countdown > 0 ? `${countdown}s` : '获取验证码' }}
          </el-button>
        </div>
      </el-form-item>

      <el-form-item prop="password">
        <el-input
          v-model="form.password"
          type="password"
          :prefix-icon="Lock"
          placeholder="设置密码（6~20 位）"
          show-password
          autocomplete="new-password"
        />
      </el-form-item>

      <el-form-item prop="nickname">
        <el-input
          v-model="form.nickname"
          :prefix-icon="User"
          placeholder="昵称（选填，不填自动生成）"
          maxlength="20"
        />
      </el-form-item>

      <el-button
        class="submit-btn"
        :loading="loading"
        @click="handleSubmit"
      >
        注 册
      </el-button>
    </el-form>
  </AuthLayout>
</template>

<style scoped lang="scss">
.code-row {
  display: flex;
  gap: 10px;
  width: 100%;

  :deep(.el-input) {
    flex: 1;
  }
}

.code-btn {
  flex: 0 0 auto;
  height: 52px;
  padding: 0 18px;
  font-size: 14px;
  color: #4b3fe4;
  background: #eef0ff;
  border: none;
  border-radius: 14px;

  &:hover:not(.is-disabled) {
    background: #e2e5ff;
  }

  &.is-disabled {
    color: #9ca3af;
    background: #f3f4f6;
  }
}
</style>
