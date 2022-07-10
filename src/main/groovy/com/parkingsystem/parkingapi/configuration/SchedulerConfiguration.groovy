package com.parkingsystem.parkingapi.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers

import java.util.concurrent.Executors

@Configuration
class SchedulerConfiguration {

    @Value("${scheduler.jdbc.thread-pool-size}")
    Integer threadPoolSize

    @Bean
    Scheduler jdbcScheduler(){
        return Schedulers.fromExecutor(Executors.newFixedThreadPool(threadPoolSize))
    }

}
