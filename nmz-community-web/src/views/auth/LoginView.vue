<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { Iphone, Lock } from '@element-plus/icons-vue'
import 'element-plus/es/components/message/style/css'
import AuthLayout from '@/layouts/AuthLayout.vue'
import { login } from '@/api/auth'
import { useUserStore } from '@/stores/user'
import type { LoginForm } from '@/types/api'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const formRef = ref<FormInstance>()
const loading = ref(false)

const form = reactive<LoginForm>({
  phone: '',
  password: ''
})

const rules: FormRules = {
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

async function handleSubmit() {
  if (!formRef.value) return
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const res = await login({ phone: form.phone, password: form.password })
    userStore.setLogin(res.data)
    ElMessage.success('登录成功')
    const redirect = (route.query.redirect as string) || '/home'
    router.replace(redirect)
  } catch {
    // 失败提示已由响应拦截器统一弹出
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <AuthLayout
    footer-text="还没有账号？"
    footer-link-text="立即注册"
    footer-to="/register"
  >
    <h2 class="form-title">登录</h2>
    <p class="form-subtitle">使用手机号和密码登录，继续你的社区之旅。</p>

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

      <el-form-item prop="password">
        <el-input
          v-model="form.password"
          type="password"
          :prefix-icon="Lock"
          placeholder="请输入密码"
          show-password
          autocomplete="current-password"
        />
      </el-form-item>

      <el-button
        class="submit-btn"
        :loading="loading"
        @click="handleSubmit"
      >
        登 录
      </el-button>
    </el-form>
  </AuthLayout>
</template>
