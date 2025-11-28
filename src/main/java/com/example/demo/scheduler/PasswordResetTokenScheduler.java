package com.example.demo.scheduler;

import com.example.demo.service.PasswordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Component
public class PasswordResetTokenScheduler {
	private static final Logger log = LoggerFactory.getLogger(PasswordResetTokenScheduler.class);

    @Autowired
    private PasswordResetTokenService tokenService;

    // 每 1 小時執行一次
    @Scheduled(cron = "0 0 */1 * * ?") // 每小時整點執行
    public void clearExpiredTokens() {
    	int deleted = tokenService.deleteExpiredTokens();
        log.info("Deleted expired tokens: {}", deleted);
    }
}
