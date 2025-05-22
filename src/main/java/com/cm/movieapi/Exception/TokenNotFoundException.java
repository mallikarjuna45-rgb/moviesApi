package com.cm.movieapi.Exception;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(String refreshTokenNotExist) {
        super("refresh token not exist: " + refreshTokenNotExist);
    }
}
