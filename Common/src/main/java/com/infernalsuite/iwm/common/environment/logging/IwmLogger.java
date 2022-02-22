package com.infernalsuite.iwm.common.environment.logging;

public interface IwmLogger {

    void info(String s);

    void warn(String s);

    void warn(String s, Throwable t);

    void severe(String s);

    void severe(String s, Throwable t);

}
