package com.infernalsuite.iwm.common.environment.logging;

import lombok.RequiredArgsConstructor;

import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class JavaEnvLogger implements IwmLogger {

    private final Logger logger;

    @Override
    public void info(String s) {
        this.logger.info(s);
    }

    @Override
    public void warn(String s) {
        this.logger.warning(s);
    }

    @Override
    public void warn(String s, Throwable t) {
        this.logger.log(Level.WARNING, s, t);
    }

    @Override
    public void severe(String s) {
        this.logger.severe(s);
    }

    @Override
    public void severe(String s, Throwable t) {
        this.logger.log(Level.SEVERE, s, t);
    }

}
