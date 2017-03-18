package com.brielmayer.teda.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Bean {
    private String beanName;
    private List<Header> header = new ArrayList<>();
    private List<Map<String, Object>> data = new ArrayList<>();

    public Bean(String beanName, List<Header> header, List<Map<String, Object>> data) {
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
