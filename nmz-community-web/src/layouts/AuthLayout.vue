<script setup lang="ts">
/**
 * 登录 / 注册共用的左右分栏布局。
 *
 * 左侧：品牌区（渐变背景 + 内嵌白色卡片）
 * 右侧：表单区，通过默认插槽传入
 */
withDefaults(
  defineProps<{
    /** 左下角提示文案，如「还没有账号？」 */
    footerText?: string
    /** 左下角按钮文案，如「立即注册」 */
    footerLinkText?: string
    /** 左下角按钮跳转路径 */
    footerTo?: string
  }>(),
  {
    footerText: '',
    footerLinkText: '',
    footerTo: '/'
  }
)
</script>

<template>
  <div class="auth-wrapper">
    <div class="auth-shell">
      <!-- 左侧品牌区 -->
      <section class="brand-panel">
        <div class="brand-card">
          <div class="brand-logo">
            <span class="logo-mark">n</span>
            <span class="logo-text">nmz 社区</span>
          </div>

          <h1 class="brand-title">你好，<br />欢迎来到 nmz 社区！</h1>

          <p class="brand-slogan">记录 · 分享 · 讨论</p>

          <p class="brand-desc">
            把零散的想法沉淀成文章，把踩过的坑变成别人脚下的台阶。
            在这里，每一个技术问题都值得被认真回答。
          </p>

          <div v-if="footerText" class="brand-footer">
            <span class="footer-tip">{{ footerText }}</span>
            <router-link class="pill-btn" :to="footerTo">
              {{ footerLinkText }}
            </router-link>
          </div>
        </div>
      </section>

      <!-- 右侧表单区 -->
      <section class="form-panel">
        <div class="form-inner">
          <slot />
        </div>
      </section>
    </div>
  </div>
</template>

<style scoped lang="scss">
.auth-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100%;
  padding: 32px;
  background: #f4f5f7;
}

.auth-shell {
  display: grid;
  grid-template-columns: 1.05fr 0.95fr;
  width: 100%;
  max-width: 1120px;
  min-height: 640px;
  background: #fff;
  border-radius: 28px;
  box-shadow: 0 24px 64px rgb(17 24 39 / 8%);
  overflow: hidden;
}

/* ---------- 左侧 ---------- */
.brand-panel {
  position: relative;
  padding: 24px;
  background: linear-gradient(
    140deg,
    #79c7f2 0%,
    #9c8ef0 48%,
    #edb4e8 100%
  );
}

/* 左上角的柔和高光，让渐变不至于太死板 */
.brand-panel::after {
  position: absolute;
  inset: 0;
  background: radial-gradient(
    60% 50% at 20% 10%,
    rgb(255 255 255 / 40%),
    transparent 70%
  );
  content: '';
  pointer-events: none;
}

.brand-card {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 44px 40px;
  background: #fff;
  border-radius: 24px;
}

.brand-logo {
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo-mark {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  font-size: 18px;
  font-weight: 700;
  color: #fff;
  background: #5b47f5;
  border-radius: 9px;
}

.logo-text {
  font-size: 18px;
  font-weight: 600;
  color: #111827;
}

.brand-title {
  margin: 40px 0 0;
  font-size: 40px;
  font-weight: 700;
  line-height: 1.3;
  color: #111827;
  letter-spacing: -0.5px;
}

.brand-slogan {
  margin: 20px 0 0;
  font-size: 16px;
  font-weight: 500;
  color: #5b47f5;
}

.brand-desc {
  margin: 16px 0 0;
  font-size: 14px;
  line-height: 1.9;
  color: #6b7280;
}

.brand-footer {
  display: flex;
  flex-direction: column;
  gap: 14px;
  align-items: flex-start;
  margin-top: auto;
  padding-top: 32px;
}

.footer-tip {
  font-size: 14px;
  color: #374151;
}

.pill-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 128px;
  height: 44px;
  padding: 0 28px;
  font-size: 14px;
  font-weight: 600;
  color: #4b3fe4;
  background: #eef0ff;
  border-radius: 999px;
  transition: background 0.2s;

  &:hover {
    background: #e2e5ff;
    text-decoration: none;
  }
}

/* ---------- 右侧 ---------- */
.form-panel {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 64px 56px;
}

.form-inner {
  width: 100%;
  max-width: 380px;
}

/* ---------- 窄屏：上下堆叠 ---------- */
@media (max-width: 900px) {
  .auth-shell {
    grid-template-columns: 1fr;
    min-height: auto;
  }

  .brand-panel {
    padding: 16px;
  }

  .brand-card {
    padding: 28px 24px;
  }

  .brand-title {
    margin-top: 20px;
    font-size: 28px;
  }

  .brand-desc,
  .brand-footer {
    display: none;
  }

  .form-panel {
    padding: 40px 24px 48px;
  }
}
</style>
