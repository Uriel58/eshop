package com.example.demo.service.impl;

import com.example.demo.dao.PasswordResetTokenDAO;
import com.example.demo.model.PasswordResetToken;
import com.example.demo.service.PasswordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    @Autowired
    private PasswordResetTokenDAO tokenDAO;

    @Override
    public void createToken(PasswordResetToken token) {
        tokenDAO.save(token);
    }

    @Override
    @Transactional(readOnly = true)
    public PasswordResetToken getByToken(String token) {
        return tokenDAO.findByToken(token);
    }

    @Override
    public void markTokenAsUsed(PasswordResetToken token) {
        token.setUsed(true);
        tokenDAO.update(token);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isTokenValid(String tokenStr) {
        PasswordResetToken token = tokenDAO.findByToken(tokenStr);
        return token != null && !token.getUsed() && token.getExpiryDate().isAfter(ZonedDateTime.now());
    }

    @Override
    public void deleteToken(PasswordResetToken token) {
        tokenDAO.delete(token);
    }

    @Override
    @Transactional
    public void deleteExpiredTokens() {
        List<PasswordResetToken> allTokens = tokenDAO.findAll();
        ZonedDateTime now = ZonedDateTime.now();
        for (PasswordResetToken token : allTokens) {
            if (token.getExpiryDate().isBefore(now)) {
                tokenDAO.delete(token);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PasswordResetToken> getAllTokens() {
        return tokenDAO.findAll();
    }
    
}
