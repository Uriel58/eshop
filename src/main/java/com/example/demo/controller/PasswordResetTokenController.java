package com.example.demo.controller;

import com.example.demo.model.PasswordResetToken;
import com.example.demo.model.User;
import com.example.demo.service.PasswordResetTokenService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * 密碼重設流程（簡化版）
 */
@RestController
@RequestMapping("/api/password-reset")
public class PasswordResetTokenController {

    @Autowired
    private PasswordResetTokenService tokenService;

    @Autowired
    private UserService userService;

    /**
     * 1️⃣ 輸入 Email → 建立驗證碼（token）
     */
    @PostMapping("/request")
    public ResponseEntity<String> requestReset(@RequestParam String email) {
    	User user = userService.findByEmail(email);
        if (user == null) {
        	return ResponseEntity.badRequest()
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .body("此 Email 不存在");
        }

        // 產生驗證碼（簡化成短一點的 UUID 片段）
        String tokenStr = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        ZonedDateTime expiryDate = ZonedDateTime.now().plusMinutes(10);

        PasswordResetToken token = new PasswordResetToken(tokenStr, user, expiryDate);
        tokenService.createToken(token);

        return ResponseEntity.ok()
                .header("Content-Type", "text/plain; charset=UTF-8")
                .body(tokenStr); // 直接回傳驗證碼（實際上應寄信）
    }

    /**
     * 2️⃣ 驗證驗證碼是否正確
     */
    @GetMapping("/validate/{token}")
    public ResponseEntity<String> validateToken(@PathVariable String token) {
        if (tokenService.isTokenValid(token)) {
        	return ResponseEntity.ok()
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .body("驗證成功");
        } else {
        	return ResponseEntity.badRequest()
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .body("驗證碼錯誤或已過期");
        }
    }

    /**
     * 3️⃣ 使用驗證碼設定新密碼
     */
    @PostMapping("/use/{token}")
    public ResponseEntity<String> resetPassword(@PathVariable String token, @RequestParam String newPassword) {
        PasswordResetToken resetToken = tokenService.getByToken(token);

        if (resetToken == null || resetToken.getUsed() || resetToken.getExpiryDate().isBefore(ZonedDateTime.now())) {
        	return ResponseEntity.badRequest()
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .body("驗證碼無效或已過期");
        }

        User user = resetToken.getUser();
        user.setPassword(newPassword);
        userService.updateUser(user.getId(), user);

        tokenService.markTokenAsUsed(resetToken);

        return ResponseEntity.ok()
                .header("Content-Type", "text/plain; charset=UTF-8")
                .body("密碼重設成功！");
    }
}
