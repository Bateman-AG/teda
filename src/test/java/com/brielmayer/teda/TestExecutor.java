package com.brielmayer.teda;

import com.brielmayer.teda.handler.ExecutionHandler;

public class TestExecutor implements ExecutionHandler {
    @Override
    public void execute(String value) {
        System.out.println("Execute: " + value);
    }
}
