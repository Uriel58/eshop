package com.example.demo.test;

import com.example.demo.config.WebMvcConfig;
import com.example.demo.model.PasswordResetToken;
import com.example.demo.model.User;
import com.example.demo.service.PasswordResetTokenService;
import com.example.demo.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebMvcConfig.class }) // 使用 Spring 設定
@WebAppConfiguration
public class PasswordResetTokenServiceTest {

    @Autowired
    private PasswordResetTokenService tokenService;

    @Autowired
    private UserService userService;

    @Test
    public void testCreateAndValidateToken() {
        // 確保依賴注入成功
        assertNotNull("PasswordResetTokenService 未注入", tokenService);
        assertNotNull("UserService 未注入", userService);

        // 取得測試用的使用者（假設資料庫已有 ID=1 的用戶）
        User user = userService.getUserById(1L);
        assertNotNull("找不到測試用使用者 (ID=1)", user);

        // 建立 Token
        String tokenStr = UUID.randomUUID().toString();
        ZonedDateTime expiryDate = ZonedDateTime.now().plusMinutes(30);
        PasswordResetToken token = new PasswordResetToken(tokenStr, user, expiryDate);
        tokenService.createToken(token);

        // 驗證 Token 是否存在
        PasswordResetToken fetched = tokenService.getByToken(tokenStr);
        assertNotNull("Token 應該存在", fetched);
        assertEquals("Token 字串不相符", tokenStr, fetched.getToken());
        assertEquals("Token 所屬使用者不正確", user.getId(), fetched.getUser().getId());

        // 驗證 Token 是否有效
        assertTrue("Token 應該是有效的", tokenService.isTokenValid(tokenStr));
    }

    @Test
    public void testMarkTokenAsUsed() {
        // 建立測試用 Token
        User user = userService.getUserById(1L);
        String tokenStr = UUID.randomUUID().toString();
        PasswordResetToken token = new PasswordResetToken(tokenStr, user, ZonedDateTime.now().plusHours(1));
        tokenService.createToken(token);

        // 標記為已使用
        tokenService.markTokenAsUsed(token);

        PasswordResetToken usedToken = tokenService.getByToken(tokenStr);
        assertTrue("Token 應標示為已使用", usedToken.getUsed());
    }

    @Test
    public void testDeleteExpiredTokens() {
        User user = userService.getUserById(1L);

        // 建立一個過期 Token
        String expiredTokenStr = UUID.randomUUID().toString();
        PasswordResetToken expiredToken = new PasswordResetToken(expiredTokenStr, user, ZonedDateTime.now().minusHours(2));
        tokenService.createToken(expiredToken);

        // 呼叫刪除過期 Token 的方法
        tokenService.deleteExpiredTokens();

        // 驗證已刪除
        PasswordResetToken deleted = tokenService.getByToken(expiredTokenStr);
        assertNull("過期 Token 應該被刪除", deleted);
    }

    @Test
    public void testGetAllTokens() {
        List<PasswordResetToken> tokens = tokenService.getAllTokens();
        assertNotNull("Token 列表不應為 null", tokens);
        assertTrue("Token 列表應該是空的或有資料", tokens.size() >= 0);
    }
}
