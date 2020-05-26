package com.daypaytechnologies.security.service;


import com.daypaytechnologies.security.domain.PlatformUser;

public interface PlatformPasswordEncoder {

    String encode(PlatformUser appUser);
}