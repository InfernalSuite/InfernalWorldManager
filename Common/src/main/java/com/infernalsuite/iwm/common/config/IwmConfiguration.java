package com.infernalsuite.iwm.common.config;

import com.infernalsuite.iwm.common.config.generic.KeyedConfiguration;
import com.infernalsuite.iwm.common.config.generic.adapter.ConfigurationAdapter;
import com.infernalsuite.iwm.common.environment.IwmEnv;
import lombok.Getter;

public class IwmConfiguration extends KeyedConfiguration {

    @Getter
    private final IwmEnv iwmEnv;

    public IwmConfiguration(IwmEnv iwmEnv, ConfigurationAdapter adapter) {
        super(adapter, ConfigKeys.getKeys());
        this.iwmEnv = iwmEnv;
    }

    @Override
    protected void load(boolean initial) {
        super.load(initial);
    }

    @Override
    public void reload() {
        super.reload();
    }
}
