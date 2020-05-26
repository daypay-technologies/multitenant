package com.daypaytechnologies.configuration.exception;


import com.daypaytechnologies.core.exception.AbstractPlatformDomainRuleException;

public class GlobalConfigurationPropertyCannotBeModfied extends AbstractPlatformDomainRuleException {
    
    public GlobalConfigurationPropertyCannotBeModfied(final Long configId) {
        super("error.msg.configuration.id.not.modifiable", "Configuration identifier `" + configId + "` cannot be modified", new Object[] {configId});
    }

}
