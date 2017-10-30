package com.flat.bargain.parser;

public enum FlatType {
    NORMAL(0), GROUND(1);


    FlatType(Integer value) {
        this.value = value;
    }

    private Integer value;

    public Integer getValue() {
        return value;
    }
}
