package com.banking.banking.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class StatisticsUpdaterRunner implements CommandLineRunner {
    @Override
    public void run(String...args) throws Exception {
        //      running the thread that will run the updater in parallel right after loading the context
        StatisticsUpdater statisticsUpdater = new StatisticsUpdater();

//      instead of running the thread with an internal infinite while loop that would be very expensive
//      I used this scheduler that controls the thread to do its job once a second (massive performance saving)
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(statisticsUpdater, 1, 1, TimeUnit.SECONDS);
        System.out.println("sucsessssssssssssss");
    }
}
