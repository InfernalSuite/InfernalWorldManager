package com.infernalsuite.api.sources.type;

import com.infernalsuite.api.sources.DataSource;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;

/**
 * File Data Source
 */
public interface FileDS extends DataSource {

    @NonNull File getWorldDir();

}
