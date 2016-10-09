package com.bcrusu.gitHubEvents.common.cli;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.util.Properties;

public abstract class CliProperties {
    private final Properties _properties;

    protected CliProperties(Properties properties) {
        _properties = properties;
    }

    public abstract boolean validate();

    protected String getStringProperty(String name) {
        return _properties.getProperty(name);
    }

    protected String getStringProperty(String name, String defaultValue) {
        return _properties.getProperty(name, defaultValue);
    }

    protected int getIntProperty(String name, int defaultValue) {
        if (!_properties.containsKey(name))
            return defaultValue;

        return Integer.parseInt(_properties.getProperty(name));
    }

    protected boolean getBoolProperty(String name, boolean defaultValue) {
        if (!_properties.containsKey(name))
            return defaultValue;

        String value = _properties.getProperty(name);
        if (value == null || value.length() == 0)
            return true; // switch property defaults to true

        return Boolean.parseBoolean(value);
    }

    protected boolean hasProperty(String name) {
        return _properties.contains(name);
    }

    protected boolean hasValidStringProperty(String name) {
        return _properties.getProperty(name) != null;
    }

    protected boolean hasValidIntProperty(String name) {
        String value = _properties.getProperty(name);
        if (value == null) return false;

        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static CliProperties parse(String[] args, Class cliPropertiesClass) {
        Properties properties = CommandLineArgsParser.parse(args);

        try {
            Constructor ctor = cliPropertiesClass.getDeclaredConstructor(Properties.class);
            ctor.setAccessible(true);
            return (CliProperties) ctor.newInstance(properties);
        } catch (Exception e) {
            throw new CliPropertiesParseException(e);
        }
    }
}
