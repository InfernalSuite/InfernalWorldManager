package com.infernalsuite.iwm.common.environment;

import com.infernalsuite.iwm.common.api.IWMApi;
import com.infernalsuite.iwm.common.api.implementation.IwmWorldAdapter;
import com.infernalsuite.iwm.common.environment.bootstrap.IwmBootstrap;
import com.infernalsuite.iwm.common.environment.logging.IwmLogger;
import com.infernalsuite.iwm.common.event.IWMEventDispatcher;
import com.infernalsuite.iwm.common.formats.IWMFormatRegistry;
import com.infernalsuite.iwm.common.loaders.IWMLoaderRegistry;
import com.infernalsuite.iwm.common.sources.IWMDataSourceRegistry;
import com.infernalsuite.iwm.common.world.IWMWorldRegistry;

public interface IwmEnv {

    IwmBootstrap getBootstrap();

    IWMWorldRegistry getWorldRegistry();

    IWMLoaderRegistry getLoaderRegistry();

    IWMDataSourceRegistry getDataSourceRegistry();

    IWMFormatRegistry getFormatRegistry();

    IWMEventDispatcher getEventDispatcher();

    <T> IwmWorldAdapter<T> getWorldAdapter(Class<T> worldClass);

    IwmLogger getLogger();

    IWMApi getApi();

}
