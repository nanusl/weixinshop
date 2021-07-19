package com.github.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author nanusl
 * @version V1.0
 * @Description
 * @date 2021-02-03 20:04
 */
@Slf4j
@EnableAsync
@Configuration
public class AsyncConfig extends AsyncConfigurerSupport {

    @Override
    public Executor getAsyncExecutor() {
        return multicasterExecutor();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (Throwable ex, Method method, Object... params) -> {
            StringBuilder parameter = new StringBuilder();
            parameter.append("\nException message: ").append(ex.getMessage()).append("\n");
            parameter.append("Method name: ").append(method.getName()).append("\n");
            parameter.append("Parameter value: ").append("\n");
            for (Object param : params) {
                parameter.append(param);
                parameter.append("\n");
            }
            parameter.append("\n");
            log.warn(parameter.toString());
        };
    }

    /**
     * 定义线程池处理事件使之支持异步
     */
    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(multicasterExecutor());
        return eventMulticaster;
    }

    @Bean
    public Executor multicasterExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(20);
        taskExecutor.setQueueCapacity(1000);
        taskExecutor.setThreadNamePrefix("TaskThread-");
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        taskExecutor.initialize();
        return taskExecutor;
    }
}
