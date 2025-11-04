package com.example.demo.dao;

import com.example.demo.model.PasswordResetToken;
import java.util.List;

public interface PasswordResetTokenDAO {

    void save(PasswordResetToken token);

    PasswordResetToken findByToken(String token);

    void update(PasswordResetToken token);

    void delete(PasswordResetToken token);
    
    List<PasswordResetToken> findAll();
}
