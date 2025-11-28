package com.example.demo.service;

import com.example.demo.model.PasswordResetToken;
import java.util.List;


public interface PasswordResetTokenService {

    void createToken(PasswordResetToken token);

    PasswordResetToken getByToken(String token);

    void markTokenAsUsed(PasswordResetToken token);

    boolean isTokenValid(String token);

    void deleteToken(PasswordResetToken token);
    
    
    List<PasswordResetToken> getAllTokens(); // 用於定時任務
    
    int deleteExpiredTokens(); // 新增刪除過期 token 方法
}
