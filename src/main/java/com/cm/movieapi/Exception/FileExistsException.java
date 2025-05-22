package com.cm.movieapi.Exception;

public class FileExistsException extends RuntimeException{
    public FileExistsException(String s) {
        super(s);
    }
}
