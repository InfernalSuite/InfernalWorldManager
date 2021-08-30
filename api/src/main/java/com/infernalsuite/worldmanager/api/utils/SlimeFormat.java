package com.infernalsuite.worldmanager.api.utils;

/**
 * Class containing some standards of the SRF.
 */
public interface SlimeFormat {

    /** First bytes of every SRF file **/
    byte[] SLIME_HEADER = new byte[] { -79, 11 };

    /** Latest version of the SRF that SWM supports **/
    byte SLIME_VERSION = 9;
}
