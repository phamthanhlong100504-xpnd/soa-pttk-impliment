package com.vn.tech.inventory_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulerConfig {
    // Chỉ cần khai báo thế này là đủ để Spring kích hoạt bộ đếm thời gian
}
