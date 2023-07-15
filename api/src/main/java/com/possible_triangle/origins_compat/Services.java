package com.possible_triangle.origins_compat;

import com.possible_triangle.origins_compat.services.IConfigs;
import com.possible_triangle.origins_compat.services.ICreateAccess;
import com.possible_triangle.origins_compat.services.IOriginsAccess;
import com.possible_triangle.origins_compat.services.IPlatformHelper;

import java.util.ServiceLoader;

public class Services {
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final ICreateAccess CREATE = load(ICreateAccess.class);
    public static final IOriginsAccess ORIGINS = load(IOriginsAccess.class);
    public static final IConfigs CONFIGS = load(IConfigs.class);
    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}