package com.brielmayer.teda;

import com.brielmayer.teda.handler.ExecutionHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogExecutionHandler implements ExecutionHandler {

    @Override
    public void execute(final String value) {
        log.info("Execute: {}", value);
    }
}
