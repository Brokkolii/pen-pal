package com.girnghuber.penpal.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AppleTokenService {

    public DecodedJWT validateAppleToken(String token) throws Exception {
        RSAKeyProvider keyProvider = new AppleKeyProvider();
        Algorithm algorithm = Algorithm.RSA256(keyProvider);
        return JWT.require(algorithm)
                .withIssuer("https://appleid.apple.com") // Verify issuer
                .build()
                .verify(token);
    }
}
