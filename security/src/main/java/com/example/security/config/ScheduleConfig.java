package com.example.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;

@Configuration
public class ScheduleConfig implements SchedulingConfigurer {

    /**
     * 向spring容器注入TaskScheduler线程池，用于执行@Scheduled注解标注的方法.
     * 类型为TaskScheduler.class, name为taskExecutor1的bean（使用类型注入spring，不是bean name）
     * 如果没有注入TaskScheduler或者ScheduledExecutorService，则默认使用单线程的线程池作为底层支撑
     * @return TaskScheduler 实例
     */
//    @Bean
//    public TaskScheduler taskExecutor() {
//        return new ConcurrentTaskScheduler(Executors.newScheduledThreadPool(3, new MyThreadFactory("scheduled")));
//    }
//
//    @Bean
//    @Qualifier("test-123")
//    public TaskScheduler taskExecutor2() {
//        return new ConcurrentTaskScheduler(Executors.newScheduledThreadPool(3, new MyThreadFactory("scheduled2")));
//    }

    /**
     * 可以用于执行定时任务，设置taskScheduler等
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //taskRegistrar.setScheduler(taskExecutor1());      用于显示的设置线程池执行器
//        taskRegistrar.addFixedDelayTask(() -> System.out.println("SchedulingConfigurer test"), 5000);
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(3));
    }

}