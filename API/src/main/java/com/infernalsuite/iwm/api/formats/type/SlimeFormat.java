package com.infernalsuite.iwm.api.formats.type;

import com.infernalsuite.iwm.api.formats.Format;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface SlimeFormat extends Format {

    @Override default @NonNull String getName() { return "slime"; }

}
