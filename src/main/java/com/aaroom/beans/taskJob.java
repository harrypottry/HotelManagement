package com.aaroom.beans;
import com.aaroom.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
/**
 * Created by 温建成 on 2019/1/7.
 * 在固定的时间执行程序
 */
@Component("taskJob")
public class taskJob {

    @Autowired
    private AccountService accountService;
    //每个月2号的凌晨执行生成账单
    //什么时候执行
    @Scheduled(cron = "0 0 4 1 * ?")
    public void job1() {
        System.out.println("任务进行中。。。");
        //调用要执行的方法
        accountService.insertAccount();
        System.out.println("任务结束。。。");
    }
}
