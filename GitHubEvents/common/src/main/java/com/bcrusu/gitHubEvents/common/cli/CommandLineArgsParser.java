package com.bcrusu.gitHubEvents.common.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

class CommandLineArgsParser {
    private static final Logger _logger = LoggerFactory.getLogger(CommandLineArgsParser.class);

    private static final String PROPERTY_NAME_PREFIX = "-P";

    public static Properties parse(String[] args) {
        Properties result = new Properties();

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (!isPropertyName(arg)) {
                _logger.warn("Skipped argument '{}'. Invalid property format.", arg);
                continue;
            }

            int equalsIndex = arg.indexOf('=');
            String propName;
            String value = "";

            if (equalsIndex >= 0) {
                propName = arg.substring(2, equalsIndex);
                value = arg.substring(equalsIndex + 1);
            } else {
                propName = arg.substring(2);
            }

            result.setProperty(propName, value);
        }

        return result;
    }

    private static boolean isPropertyName(String arg) {
        return arg != null && arg.startsWith(PROPERTY_NAME_PREFIX);
    }
}
