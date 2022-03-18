package com.infernalsuite.iwm.api.utils;

import lombok.Getter;

/**
 * Credits to Minikloon for this class.
 *
 * <p>Source: https://github.com/Minikloon/CraftyWorld/blob/master/crafty-common/src/main/kotlin/world/crafty/common/utils/NibbleArray.kt</p>
 */
public class NibbleArray {

    @Getter
    private final byte[] backingArray;

    public NibbleArray(int size) {
        this(new byte[size / 2]);
    }

    public NibbleArray(byte[] backingArray) {
        this.backingArray = backingArray;
    }

    public int get(int index) {
        int value = this.backingArray[index / 2];
        return index % 2 == 0 ? value & 0xF : (value & 0xF0) >> 4;
    }

    public void set(int index, int value) {
        int nibble = value & 0xF;
        int halfIndex = index / 2;
        int previous = this.backingArray[halfIndex];

        if (index % 2 == 0) {
            this.backingArray[halfIndex] = (byte) (previous & 0xF0 | nibble);
        }
    }

}
