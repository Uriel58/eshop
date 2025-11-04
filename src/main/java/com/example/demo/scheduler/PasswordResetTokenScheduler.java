package com.example.demo.scheduler;

import com.example.demo.service.PasswordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetTokenScheduler {

    @Autowired
    private PasswordResetTokenService tokenService;

    // 每 1 小時執行一次
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void removeExpiredTokens() {
        tokenService.deleteExpiredTokens();
        System.out.println("Expired password reset tokens cleaned up.");
    }
}
