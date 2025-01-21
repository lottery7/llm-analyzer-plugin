package com.lotterydev.util;

import java.io.InputStream;
import java.util.Objects;

public class Resources {
    public static InputStream getResourceInputStream(String resourceName) {
        try {
            if (!resourceName.startsWith("/")) {
                resourceName = "/" + resourceName;
            }
            return Objects.requireNonNull(Resources.class.getResourceAsStream(resourceName));
        } catch (NullPointerException e) {
            throw new RuntimeException(String.format("Resource \"%s\" is not found.", resourceName), e);
        }
    }
}
