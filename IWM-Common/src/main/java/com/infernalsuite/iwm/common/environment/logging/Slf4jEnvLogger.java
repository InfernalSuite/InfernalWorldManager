package com.infernalsuite.iwm.common.environment.logging;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

@RequiredArgsConstructor
public class Slf4jEnvLogger implements IwmLogger {

    private final Logger logger;

    @Override
    public void info(String s) {
        this.logger.info(s);
    }

    @Override
    public void warn(String s) {
        this.logger.warn(s);
    }

    @Override
    public void warn(String s, Throwable t) {
        this.logger.warn(s, t);
    }

    @Override
    public void severe(String s) {
        this.logger.error(s);
    }

    @Override
    public void severe(String s, Throwable t) {
        this.logger.error(s, t);
    }
}
