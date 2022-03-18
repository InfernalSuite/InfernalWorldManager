package com.infernalsuite.iwm.common.api;

import com.infernalsuite.iwm.api.IWMProvider;
import com.infernalsuite.iwm.api.InfernalWorldManager;

import java.lang.reflect.Method;

/**
 * Utility class for registering the API instance with the {@link IWMProvider API provider}
 */
public class ApiRegistrationUtil {

    private static final Method REGISTER;
    private static final Method UNREGISTER;

    static {
        try {
            REGISTER = IWMProvider.class.getDeclaredMethod("register", InfernalWorldManager.class);
            REGISTER.setAccessible(true);

            UNREGISTER = IWMProvider.class.getDeclaredMethod("unregister");
            UNREGISTER.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void registerApi(InfernalWorldManager iwmApi) {
        try {
            REGISTER.invoke(null, iwmApi);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unregisterApi() {
        try {
            UNREGISTER.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
