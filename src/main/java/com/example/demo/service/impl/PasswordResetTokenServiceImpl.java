package com.example.demo.service.impl;

import com.example.demo.dao.PasswordResetTokenDAO;
import com.example.demo.model.PasswordResetToken;
import com.example.demo.service.PasswordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import org.hibernate.StaleStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
	
	 private static final Logger log = LoggerFactory.getLogger(PasswordResetTokenServiceImpl.class);

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
                try {
                    tokenDAO.delete(token); // DAO 已經做 merge 保護
                    log.info("Deleted expired token: {}", token.getId());
                } catch (StaleStateException e) {
                    log.warn("Token {} already deleted, skipping.", token.getId());
                } catch (Exception e) {
                    log.error("Unexpected error deleting token: {}", token.getId(), e);
                }
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PasswordResetToken> getAllTokens() {
        return tokenDAO.findAll();
    }
    
}
