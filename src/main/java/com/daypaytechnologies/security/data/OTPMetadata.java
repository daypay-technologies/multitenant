package com.daypaytechnologies.security.data;

import org.joda.time.DateTime;

public class OTPMetadata {

    private final DateTime requestTime;
    private final int tokenLiveTimeInSec;
    private final boolean extendedAccessToken;
    private final OTPDeliveryMethod deliveryMethod;

    public OTPMetadata(DateTime requestTime, int tokenLiveTimeInSec,
                       boolean extendedAccessToken, OTPDeliveryMethod deliveryMethod) {
        this.requestTime = requestTime;
        this.tokenLiveTimeInSec = tokenLiveTimeInSec;
        this.extendedAccessToken = extendedAccessToken;
        this.deliveryMethod = deliveryMethod;
    }

    public DateTime getRequestTime() {
        return requestTime;
    }

    public int getTokenLiveTimeInSec() {
        return tokenLiveTimeInSec;
    }

    public boolean isExtendedAccessToken() {
        return extendedAccessToken;
    }

    public OTPDeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }
}
