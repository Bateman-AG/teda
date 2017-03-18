package com.brielmayer.teda.exception;

public class ParseException extends TedaException {
    public ParseException(String s, Object... params) {
        super(s, params);
    }

    public ParseException(Throwable t, String s, Object... params) {
        super(t, s, params);
    }
}
