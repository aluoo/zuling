package com.anyi.sparrow.base.configure;

import com.anyi.sparrow.base.security.LoginUserContext;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDateTime;
import java.util.Date;


@EnableTransactionManagement
@Configuration
public class MybatisConfig {

    /**
     * 分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }


    @Slf4j
    @Component
    public static class MyMetaObjectHandler implements MetaObjectHandler {


        @Override
        public void insertFill(MetaObject metaObject) {

            log.info("start insert fill ....");
            if (metaObject.hasGetter("createBy")) {
                this.setFieldValByName("createBy", LoginUserContext.getUserIdByCatch(), metaObject);
            }
//            this.setFieldValByName("createTime", new Date(), metaObject);
//            this.setFieldValByName("updateTime", new Date(), metaObject);
            this.strictInsertFill(metaObject,"createTime", ()-> new Date(), Date.class);
            this.strictInsertFill(metaObject,"createTime", ()-> LocalDateTime.now(), LocalDateTime.class);
            this.strictInsertFill(metaObject,"updateTime", ()-> new Date(), Date.class);
            this.strictInsertFill(metaObject,"updateTime", ()-> LocalDateTime.now(), LocalDateTime.class);

            if (metaObject.hasGetter("deleted")) {
                this.setFieldValByName("deleted", false, metaObject);
            }
        }

        @Override
        public void updateFill(MetaObject metaObject) {
            log.info("start update fill ....");
            this.setFieldValByName("updateTime", new Date(), metaObject);
            this.setFieldValByName("updateBy", LoginUserContext.getUserIdByCatch(), metaObject);
        }

    }
}