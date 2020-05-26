package com.daypaytechnologies.configuration.domain;

import com.daypaytechnologies.configuration.exception.GlobalConfigurationPropertyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Wrapper for {@link GlobalConfigurationRepository} that adds NULL checking and
 * Error handling capabilities
 * </p>
 */
@Service
public class GlobalConfigurationRepositoryWrapper {

    private final GlobalConfigurationRepository repository;

    @Autowired
    public GlobalConfigurationRepositoryWrapper(final GlobalConfigurationRepository repository) {
        this.repository = repository;
    }

    public GlobalConfigurationProperty findOneByNameWithNotFoundDetection(final String propertyName) {
        final GlobalConfigurationProperty property = this.repository.findOneByName(propertyName);
        if (property == null) { throw new GlobalConfigurationPropertyNotFoundException(propertyName); }
        return property;
    }

    public GlobalConfigurationProperty findOneWithNotFoundDetection(final Long configId) {
        final GlobalConfigurationProperty property = this.repository.findOne(configId);
        if (property == null) { throw new GlobalConfigurationPropertyNotFoundException(configId); }
        return property;
    }

    public void save(final GlobalConfigurationProperty globalConfigurationProperty) {
        this.repository.save(globalConfigurationProperty);
    }

    public void saveAndFlush(final GlobalConfigurationProperty globalConfigurationProperty) {
        this.repository.saveAndFlush(globalConfigurationProperty);
    }

    public void delete(final GlobalConfigurationProperty globalConfigurationProperty) {
        this.repository.delete(globalConfigurationProperty);
    }

}