package com.cm.movieapi.Exception;

public class TokenExpiredException extends RuntimeException{
    public TokenExpiredException(String refreshTokenExpired) {
        super(refreshTokenExpired);
    }
}
