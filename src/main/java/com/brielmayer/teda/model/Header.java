package com.brielmayer.teda.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Header {

    private final String name;
    private final boolean primaryKey;
}
