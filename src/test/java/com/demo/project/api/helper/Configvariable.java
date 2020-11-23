package com.demo.project.api.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class Configvariable {
    private static final Logger logger = LogManager.getLogger(Configvariable.class);

    //for global variables
    public static Map<String, String> globalPropertyMap = new HashMap<>();
    //for properties files
    public static Map<String, String> envPropertyMap = new HashMap<>();

    public String expandValue(String expression) {
        String originalExp = expression;
        if (expression == null)
            return null;
        int varStart = expression.indexOf("${");
        if (varStart >= 0) {
            String varName = "";
            String expanded = "";
            while (varStart >= 0) {
                int varEnd = originalExp.indexOf('}', varStart + 2);
                if (varEnd > varStart + 1) {
                    varName = originalExp.substring(varStart + "${".length(), varEnd);
                    String value = this.getStringVar(varName);
                    expanded = originalExp.substring(0, varStart) + value + originalExp.substring(varEnd + 1);
                }
                originalExp = expanded;
                varStart = expanded.indexOf("${");
            }
            return expanded;
        }
        return originalExp;
    }

    public String getStringVar(String variable) {
        String result = "";
        if (globalPropertyMap.containsKey(variable)) {
            result = globalPropertyMap.get(variable);
        }
        return result;
    }

    public void setStringVariable(String value, String variable) {
        if (globalPropertyMap.containsKey(variable)) {
            globalPropertyMap.remove(variable);
            globalPropertyMap.put(variable, value);
        } else {
            globalPropertyMap.put(variable, value);
        }
    }

    public void assignValueToVar(String value, String variable) {
        String expandedVal = expandValue(value);
        setStringVariable(expandedVal, variable);
    }

    //for gen random numbers
    public String generateRandomNumber(String format) {
        DateTimeFormatter dateTimeFormatter;
        dateTimeFormatter = DateTimeFormatter.ofPattern(format, Locale.getDefault());
        logger.info("Random number generated");
        return dateTimeFormatter.format(LocalDateTime.now());
    }

    public void setupEnvironmentProperties(String envName) {
        InputStream initialStream = null;
        if ("qa".equalsIgnoreCase(envName)) {
            initialStream = getClass().getResourceAsStream("/env/qa.properties");
        } else if ("dev".equalsIgnoreCase(envName)) {
            initialStream = getClass().getResourceAsStream("/env/uat.properties");
        } else if ("prod".equalsIgnoreCase(envName)) {
            initialStream = getClass().getResourceAsStream("/env/prod.properties");
        } else {
            initialStream = getClass().getResourceAsStream("/env/qa.properties");
        }
        try {
            Properties envProp = new Properties();
            envProp.load(initialStream);
            for (String key : envProp.stringPropertyNames()) {
                envPropertyMap.put(key, envProp.getProperty(key));
                setStringVariable(envProp.getProperty(key), key);
            }
        } catch (IOException e) {
            logger.error("Failed to load environment properties file for environment:" + initialStream);
        }
    }

}

