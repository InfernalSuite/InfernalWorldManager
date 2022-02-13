package com.infernalsuite.api;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Provides static access to the {@link InfernalWorldManager} API.
 *
 */
public final class IWMProvider {

    private static InfernalWorldManager instance = null;

    /**
     * Gets an instance of the InfernalWorldManager API,
     * throwing {@link NotLoadedException} if the API is not loaded yet.
     *
     * <p>This method will never return null.</p>
     *
     * @return an instance of the InfernalWorldManager API
     * @throws NotLoadedException if the API is not loaded yet
     */
    public static @NotNull InfernalWorldManager get() {
        InfernalWorldManager instance = IWMProvider.instance;
        if (instance == null) throw new NotLoadedException();
        return instance;
    }

    @ApiStatus.Internal
    static void register(InfernalWorldManager instance) {
        IWMProvider.instance = instance;
    }

    @ApiStatus.Internal
    static void unregister() {
        IWMProvider.instance = null;
    }

    @ApiStatus.Internal
    private IWMProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    private static final class NotLoadedException extends IllegalStateException {
        private static final String MESSAGE = """
                The InfernalWorldManager API isn't loaded yet!
                This could be because:
                  a) the InfernalWorldManager plugin is not installed or it failed to enable
                  b) the plugin in the stacktrace does not declare a dependency on LuckPerms
                  c) the plugin in the stacktrace is retrieving the API before the plugin 'enable' phase
                     (call the #get method in onEnable, not the constructor!)
                """;
        NotLoadedException() {
            super(MESSAGE);
        }
    }

}
