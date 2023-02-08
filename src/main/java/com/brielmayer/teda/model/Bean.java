package com.brielmayer.teda.model;

import java.util.List;
import java.util.Map;


public class Bean {
    private final String beanName;
    private final List<Header> header;
    private final List<Map<String, Object>> data;

    public Bean(final String beanName, final List<Header> header, final List<Map<String, Object>> data) {
        this.beanName = beanName;
        this.header = header;
        this.data = data;
    }

    public String getBeanName() {
        return beanName;
    }

    public List<Header> getHeader() {
        return header;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }
}
