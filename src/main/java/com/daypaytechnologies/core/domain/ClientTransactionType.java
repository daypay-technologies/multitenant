package com.daypaytechnologies.core.domain;

import java.util.HashMap;
import java.util.Map;

public enum ClientTransactionType {

    PAY_CHARGE(1, "clientTransactionType.payCharge"), //
    WAIVE_CHARGE(2, "clientTransactionType.waiveCharge");

    private final Integer value;
    private final String code;

    private ClientTransactionType(final Integer value, final String code) {
        this.value = value;
        this.code = code;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getCode() {
        return this.code;
    }

    private static final Map<Integer, ClientTransactionType> intToEnumMap = new HashMap<>();
    private static int minValue;
    private static int maxValue;

    static {
        int i = 0;
        for (final ClientTransactionType type : ClientTransactionType.values()) {
            if (i == 0) {
                minValue = type.value;
            }
            intToEnumMap.put(type.value, type);
            if (minValue >= type.value) {
                minValue = type.value;
            }
            if (maxValue < type.value) {
                maxValue = type.value;
            }
            i = i + 1;
        }
    }

    public static ClientTransactionType fromInt(final int i) {
        final ClientTransactionType type = intToEnumMap.get(Integer.valueOf(i));
        return type;
    }

    public static int getMinValue() {
        return minValue;
    }

    public static int getMaxValue() {
        return maxValue;
    }

    @Override
    public String toString() {
        return name().toString();
    }

}
