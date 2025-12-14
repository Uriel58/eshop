
package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileUploadConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir:D:/uploads/products}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // ⭐ 關鍵配置：將 /uploads/** 的請求映射到本地資料夾
        // 注意：file: 協議需要三個斜線（file:///）在 Windows 上
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///" + uploadDir + "/");
        
        System.out.println("========== 檔案上傳配置 ==========");
        System.out.println("上傳目錄: " + uploadDir);
        System.out.println("URL 映射: /uploads/** -> file:///" + uploadDir + "/");
        System.out.println("===================================");
    }

    public String getUploadDir() {
        return uploadDir;
    }
}