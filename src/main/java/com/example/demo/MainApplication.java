package com.example.demo;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.example.demo.config.AppConfig;

public class MainApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        System.out.println("Spring container started. Scheduling tasks...");
        
        // 保持程式運行
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        context.close();
    }
}

