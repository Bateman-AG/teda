package com.brielmayer.teda.exception;

public class TedaException extends RuntimeException {

    public TedaException(String s, Object... params) {
        super(String.format(s, params));
    }

    public TedaException(Throwable t, String s, Object... params) {
        super(String.format(s, params), t);
    }
}
