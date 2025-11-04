package com.example.demo.service;

import com.example.demo.model.PasswordResetToken;
import java.util.List;

public interface PasswordResetTokenService {

    void createToken(PasswordResetToken token);

    PasswordResetToken getByToken(String token);

    void markTokenAsUsed(PasswordResetToken token);

    boolean isTokenValid(String token);

    void deleteToken(PasswordResetToken token);
    
    void deleteExpiredTokens(); // 新增刪除過期 token 方法

    List<PasswordResetToken> getAllTokens(); // 用於定時任務
}
