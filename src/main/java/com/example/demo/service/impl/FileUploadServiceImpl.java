package com.example.demo.service.impl;

import com.example.demo.service.FileUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    @Value("${file.upload-dir:D:/uploads/products}")
    private String uploadDir;

    @Override
    public String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            logger.warn("嘗試儲存空檔案");
            throw new IOException("檔案為空，無法儲存");
        }

        // 建立上傳目錄（如果不存在）
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                logger.info("建立上傳目錄: {}", uploadDir);
            } else {
                logger.error("無法建立上傳目錄: {}", uploadDir);
                throw new IOException("無法建立上傳目錄");
            }
        }

        // 取得原始檔名和副檔名
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 產生唯一檔名：UUID + 時間戳記 + 副檔名
        String newFilename = UUID.randomUUID().toString()
                           + "_" + System.currentTimeMillis()
                           + extension;

        // 儲存檔案
        Path targetPath = Paths.get(uploadDir, newFilename);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        logger.info("檔案儲存成功: {}", newFilename);

        // 回傳相對路徑（用於前端顯示）
        return "/uploads/" + newFilename;
    }

    @Override
    public void deleteFile(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            logger.debug("圖片路徑為空，略過刪除");
            return;
        }

        try {
            String filename;
            
            // ⭐ 修正：處理不同格式的圖片路徑
            if (imagePath.startsWith("/uploads/")) {
                // 正確格式：/uploads/xxx.jpg
                filename = imagePath.substring("/uploads/".length());
            } else if (imagePath.contains("/")) {
                // 完整路徑：D:/uploads/products/xxx.jpg
                filename = imagePath.substring(imagePath.lastIndexOf("/") + 1);
            } else if (imagePath.contains("\\")) {
                // Windows 路徑：D:\ uploads\products\xxx.jpg
                filename = imagePath.substring(imagePath.lastIndexOf("\\") + 1);
            } else {
                // 純檔名：xxx.jpg
                filename = imagePath;
            }
            
            // 建立完整路徑
            Path filePath = Paths.get(uploadDir, filename);
            
            logger.info("嘗試刪除檔案: {} (完整路徑: {})", filename, filePath);

            boolean deleted = Files.deleteIfExists(filePath);
            if (deleted) {
                logger.info("檔案刪除成功: {}", filename);
            } else {
                logger.warn("檔案不存在: {} (路徑: {})", filename, filePath);
            }
        } catch (IOException e) {
            logger.error("刪除檔案失敗: {}, 錯誤訊息: {}", imagePath, e.getMessage());
        }
    }

    @Override
    public String getUploadDir() {
        return uploadDir;
    }
}