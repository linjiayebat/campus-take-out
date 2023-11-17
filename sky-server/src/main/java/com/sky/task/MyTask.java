package com.sky.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j

/**
 * 自定义定时任务类
 */

public class MyTask {

    @Scheduled(cron = "0/5 * * * * ? ")
    public void excuteTask() {
        log.info("执行时间：{}", new Date());
    }

}
