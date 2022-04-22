package com.fengxue.javax_backend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fengxue.javax_backend.entity.UserAccount;
import com.fengxue.javax_backend.entity.UserLoginTransfer;

public class TokenService {
    public String getToken(UserAccount user) {
        String token="";
        token= JWT.create().withAudience(user.getUname())
                .sign(Algorithm.HMAC256(user.getUpw()));
        return token;
    }
}
