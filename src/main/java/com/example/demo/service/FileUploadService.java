package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
 * 檔案上傳服務介面
 */
public interface FileUploadService {
    
    /**
     * 儲存上傳的檔案
     * @param file 上傳的檔案
     * @return 儲存後的檔案相對路徑（例如：/uploads/xxx.jpg）
     * @throws IOException 檔案儲存失敗時拋出異常
     */
    String saveFile(MultipartFile file) throws IOException;
    
    /**
     * 刪除指定路徑的檔案
     * @param imagePath 圖片相對路徑（例如：/uploads/xxx.jpg）
     */
    void deleteFile(String imagePath);
    
    /**
     * 取得上傳目錄路徑
     * @return 上傳目錄的絕對路徑
     */
    String getUploadDir();
}