package com.brielmayer.teda;

import com.brielmayer.teda.handler.ExecutionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogExecutionHandler implements ExecutionHandler {

    private final Logger log  = LoggerFactory.getLogger(LogExecutionHandler.class);

    @Override
    public void execute(String value) {
        log.info("Execute: {}", value);
    }
}
