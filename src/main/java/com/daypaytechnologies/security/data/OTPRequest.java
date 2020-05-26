package com.daypaytechnologies.security.data;

import com.daypaytechnologies.core.service.DateUtils;
import org.joda.time.DateTime;

public class OTPRequest {

    private final String token;
    private final OTPMetadata metadata;

    public OTPRequest(String token, OTPMetadata metadata) {
        this.token = token;
        this.metadata = metadata;
    }

    public static OTPRequest create(String token, int tokenLiveTimeInSec, boolean extendedAccessToken,
                                    OTPDeliveryMethod deliveryMethod) {
        final OTPMetadata metadata = new OTPMetadata(DateUtils.getLocalDateTimeOfTenant().toDateTime(),
                tokenLiveTimeInSec, extendedAccessToken, deliveryMethod);
        return new OTPRequest(token, metadata);
    }

    public String getToken() {
        return token;
    }

    public OTPMetadata getMetadata() {
        return metadata;
    }

    public boolean isValid() {
        DateTime expireTime = metadata.getRequestTime().plusSeconds(metadata.getTokenLiveTimeInSec());
        return DateTime.now().isBefore(expireTime);
    }
}
