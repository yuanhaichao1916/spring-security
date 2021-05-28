package com.example.security.cron;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
public class cronTest {
    int i =0;
    @Scheduled(cron = "* 30 * * * *")
    @Async
    public void  test1() throws InterruptedException {
        System.out.println(Thread.currentThread().getName());
        Thread.sleep(1000000);
        i++;
        if(i>3){
           int a = 0/0;
        }
        System.out.println(222);
    }

    @Scheduled(cron = "* 50 * * * *")
    public void  test2() throws InterruptedException {
        System.out.println(Thread.currentThread().getName());
        Thread.sleep(5);
        System.out.println(444);
    }
}
