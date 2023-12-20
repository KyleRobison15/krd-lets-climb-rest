package com.krd.letsclimbrest.constants;

import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;

public class SecurityConstants {
    public static final long JWT_EXPIRATION = 70000;
    public static final SecretKey KEY = Jwts.SIG.HS256.key().build();
}
