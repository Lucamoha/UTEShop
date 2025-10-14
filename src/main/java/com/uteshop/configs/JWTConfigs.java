package com.uteshop.configs;

import lombok.Getter;

import java.io.IOException;
import java.util.Properties;

public class JWTConfigs {
    private static final Properties properties = new Properties();
    @Getter
    private static String signerKey;
    @Getter
    private static long expiration;

    static {
        try (var is = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties")) {
            properties.load(is);
            signerKey = properties.getProperty("jwt.signerKey");
            expiration = Long.parseLong(properties.getProperty("jwt.expiration"));
        } catch (IOException e) {
            throw new RuntimeException("Không thể đọc file config.properties", e);
        } catch (NumberFormatException e) {
            throw new RuntimeException("jwt.expiration phải là số hợp lệ", e);
        }
    }
}
