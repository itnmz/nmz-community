package com.itnmz.community.auth.context;

/**
 * ⭐当前登录用户的线程上下文。
 *
 * 只在一次请求内有效，由拦截器写入、请求结束时清理。
 * 使用约定（非常重要）：
 *  1. 写入方必须负责清理，写在拦截器的 afterCompletion 里；
 *  2. 不要在异步线程 / 线程池任务里读取，ThreadLocal 不会自动传递；
 *  3. 只存 userId，不要存 User 实体——实体是可变的，容易读到脏数据。
 */
public final class UserContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();

    private UserContext() {
        // 工具类，禁止实例化
    }


    /**
     * 1、
     */
    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    /**
     * 2、取当前登录用户 ID。
     * 未登录时返回 null，调用方自己决定要不要抛业务异常。
     */
    public static Long getUserId() {
        return USER_ID.get();
    }

    /**
     * 3、清理，必须调用。Tomcat 线程是复用的，
     * 漏掉这一步会把上一个用户的 ID 带给下一个请求，属于越权级事故。
     */
    public static void clear() {
        USER_ID.remove();
    }



    /**
     * 是否已登录（仅指上下文里有值，不代表 token 一定合法）。
     */
    public static boolean isLogin() {
        return USER_ID.get() != null;
    }
}
