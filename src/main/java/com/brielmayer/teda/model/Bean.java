package com.brielmayer.teda.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class Bean {
    private final String beanName;
    private final List<Header> header;
    private final List<Map<String, Object>> data;
}
