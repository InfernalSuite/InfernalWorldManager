package com.infernalsuite.iwm.api.sources.type;

import com.infernalsuite.iwm.api.sources.DataSource;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;

/**
 * File Data Source
 */
public interface FileDS extends DataSource {

    @NonNull File getWorldDir();

}
