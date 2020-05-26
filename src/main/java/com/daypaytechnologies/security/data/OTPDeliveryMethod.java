package com.daypaytechnologies.security.data;

public class OTPDeliveryMethod {

    private final String name;
    private final String target;

    public OTPDeliveryMethod(String name, String target) {
        this.name = name;
        this.target = target;
    }

    public String getName() {
        return name;
    }

    public String getTarget() {
        return target;
    }
}
