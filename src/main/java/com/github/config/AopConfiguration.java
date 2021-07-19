package com.github.config;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author nanusl
 * @version V1.0
 * @Description
 * @date 2021-02-03 20:22
 */
@Configuration
// @EnableAspectJAutoProxy
public class AopConfiguration {

    /*@Bean
    public Advisor traceAdvisor(ApplicationEventPublisher eventPublisher) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(public * com.jfona.sifh.controller.*.*(..)) && !@annotation(com.jfona.sifh.trace.ClearTrace)");
        return new DefaultPointcutAdvisor(pointcut, new TraceInterceptor(eventPublisher));
    }*/

}
