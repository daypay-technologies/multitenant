package com.daypaytechnologies.security.data;

import org.joda.time.DateTime;

public class AccessTokenData {

    private final String token;

    private final DateTime validFrom;
    private final DateTime validTo;

    public AccessTokenData(String token, DateTime validFrom, DateTime validTo) {
        this.token = token;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    public String getToken() {
        return token;
    }

    public DateTime getValidFrom() {
        return validFrom;
    }

    public DateTime getValidTo() {
        return validTo;
    }
}
