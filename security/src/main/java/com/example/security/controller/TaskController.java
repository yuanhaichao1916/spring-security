package com.example.security.controller;

import net.minidev.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.DelegatingErrorHandlingRunnable;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

@RestController
@RequestMapping("/task")
public class TaskController {

    ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler(); //这是spring默认的 定时任务执行器  里面有个Executor 就是一个定时任务具体的执行器，是个AQS队列

    @RequestMapping("/list")
    public JSONArray getTaskList(HttpServletRequest request, HttpServletResponse response)throws Exception{
        BlockingQueue<Runnable> queue =threadPoolTaskScheduler.getScheduledThreadPoolExecutor().getQueue();//获取AQS队列，里面有我们所有的定时任务
        JSONArray tasklist=new JSONArray();
        Iterator<Runnable> iterator = queue.iterator();//循环取出定时任务
        while (iterator.hasNext()){
            Runnable runnable = iterator.next();//虽然是个Runnable对象 ，但是通过反射，可以获取更多的内容。（这里多亏了idea  可以调试查看对象的class信息）
            Field field = runnable.getClass().getSuperclass().getDeclaredField("callable");
            field.setAccessible(true);
            Object o1 = field.get(runnable);
            Class<?> aClass = Class.forName("java.util.concurrent.Executors$RunnableAdapter");//这个RunnableAdapter 是个Executors的内部类，修饰符static final class。所以使用了forName的方法是获取class对象 
            Field taskField = aClass.getDeclaredField("task");
            taskField.setAccessible(true);
            Object o2 = taskField.get(o1);
            Class o2Class = o2.getClass();
            Field delegateField;
            if (o2Class.equals( DelegatingErrorHandlingRunnable.class)){
                 delegateField= o2Class.getDeclaredField("delegate");
                delegateField.setAccessible(true);
            }else{
                delegateField = o2Class.getSuperclass().getDeclaredField("delegate");
                delegateField.setAccessible(true);
            }
            ScheduledMethodRunnable scheduledMethodRunnable=(ScheduledMethodRunnable)delegateField.get(o2);
            Method method = scheduledMethodRunnable.getMethod();
            JSONObject data=new JSONObject();
            data.put("ScheduleClassName",method.getDeclaringClass().getName());
            data.put("ScheduleMethodName",method.getName());
//            data.put("status",JSONObject.fromObject(runnable));
            tasklist.add(data);
        }
        return tasklist;
    }
}