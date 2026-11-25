<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import 'element-plus/es/components/message/style/css'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

function handleLogout() {
  userStore.logout()
  ElMessage.success('已退出登录')
  router.replace({ name: 'login' })
}
</script>

<template>
  <div class="layout">
    <header class="layout-header">
      <span class="logo">nmz 社区</span>
      <div class="user-area">
        <span class="nickname">{{ userStore.nickname || userStore.userInfo?.phone }}</span>
        <el-button link type="primary" @click="handleLogout">退出登录</el-button>
      </div>
    </header>

    <main class="layout-main">
      <el-card shadow="never">
        <template #header>登录成功</template>
        <p>你好，<strong>{{ userStore.nickname }}</strong>！</p>
        <p class="hint">
          当前用户 ID：{{ userStore.userInfo?.id }}，角色：{{ userStore.userInfo?.role }}
        </p>
        <p class="hint">帖子、评论等功能还在开发中。</p>
      </el-card>
    </main>
  </div>
</template>

<style scoped lang="scss">
.layout {
  display: flex;
  flex-direction: column;
  min-height: 100%;
}

.layout-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 56px;
  padding: 0 24px;
  background: #fff;
  border-bottom: 1px solid #ebeef5;
}

.logo {
  font-size: 17px;
  font-weight: 600;
}

.user-area {
  display: flex;
  align-items: center;
  gap: 12px;
}

.nickname {
  font-size: 14px;
  color: #606266;
}

.layout-main {
  flex: 1;
  width: 100%;
  max-width: 960px;
  margin: 24px auto;
  padding: 0 16px;
}

.hint {
  font-size: 13px;
  color: #909399;
}
</style>
