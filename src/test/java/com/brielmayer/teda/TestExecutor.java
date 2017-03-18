package com.brielmayer.teda;

import com.brielmayer.teda.handler.ExecutionHandlerI;

public class TestExecutor implements ExecutionHandlerI {
    @Override
    public void execute(String value) {
        System.out.println("Execute: " + value);
    }
}
