package cn.hanglok.algoSched.service;

import cn.hanglok.algoSched.config.TokenConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Allen
 * @version 1.0
 * @className TokenService
 * @description TODO
 * @date 2024/1/28
 */
@Service
public class TokenService {

    @Autowired
    TokenConfig tokenConfig;

    public boolean validateToken(String token) {
        return tokenConfig.getValue().equals(token);
    }
}
