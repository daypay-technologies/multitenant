package com.daypaytechnologies.user.exception;


import com.daypaytechnologies.core.exception.AbstractPlatformResourceNotFoundException;

/**
 * A {@link RuntimeException} thrown when permission resources are not found.
 */
public class PermissionNotFoundException extends AbstractPlatformResourceNotFoundException {

    public PermissionNotFoundException(final String code) {
        super("error.msg.permission.code.invalid", "Permission with Code " + code + " does not exist", code);
    }
}