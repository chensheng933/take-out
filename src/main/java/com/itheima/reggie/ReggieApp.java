package com.itheima.reggie;

import com.itheima.reggie.test.MyImportSelector;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@MapperScan("com.itheima.reggie.mapper")
//开启web组件的扫描  可以识别 @WebFilter  @WebServlet @WebListener
@ServletComponentScan("com.itheima.reggie.filter")
//@Import(MyImportSelector.class)
@EnableTransactionManagement //开启事务支持
@EnableCaching//开启缓存支持
public class ReggieApp {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApp.class,args);
        // debug(开发中) < info(开发中) < warn(上线) < error(上线)
        // 输出日志的时候,只会输出 日志级别>=该日志级别的日志
        log.info("----------info项目启动成功了----------");
    }
}
