package com.github.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.ScheduledMethodRunnable;

import java.util.List;

/**
 * @author nanusl
 * @version V1.0
 * @Description 调度
 * @date 2021-02-03 20:20
 */
@Slf4j
@Configuration
@EnableScheduling
public class SchedulingConfiguration implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar paramScheduledTaskRegistrar) {
        List<CronTask> cronTask_list = paramScheduledTaskRegistrar.getCronTaskList();
        cronTask_list.forEach(cronTask -> {
            log.error(((ScheduledMethodRunnable) cronTask.getRunnable()).getMethod() + "::" + cronTask.getExpression());
        });

    }


    @Bean("schedulerExecutor")
    public TaskScheduler poolScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(16);
        scheduler.setThreadNamePrefix("schedulerExecutor-");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(60);
        return scheduler;
    }

   /* @Scheduled(fixedDelay = 500)
    public void retrieveCountry() {
        System.err.println("SchedulingConfiguration.retrieveCountry");
    }*/
}
