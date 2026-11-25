package com.itnmz.community.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus 配置。
 *
 * 注意：PaginationInnerInterceptor 从 MP 3.5.9 起被拆到 mybatis-plus-jsqlparser 包，
 * pom.xml 里少了那个依赖，这个类会报 ClassNotFoundException。
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        //interceptor：拦截器
        //初始化核心插件
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        //1.创建分页插件
        PaginationInnerInterceptor pagination = new PaginationInnerInterceptor(DbType.MYSQL);
        // 单页最大条数：防止前端传 pageSize=999999 把数据库打穿
        pagination.setMaxLimit(1000L);
        // 页码越界时返回空列表，而不是自动跳回第一页
        pagination.setOverflow(false);

        //2.添加分页插件
        interceptor.addInnerInterceptor(pagination);
        return interceptor;
    }
}
