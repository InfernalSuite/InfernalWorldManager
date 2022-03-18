package com.infernalsuite.iwm.api.utils;

import lombok.experimental.UtilityClass;
import org.jglrxavpok.hephaistos.nbt.*;

import java.util.Optional;

/**
 * Utility class to help type conversions of NBT Tag Types, using {@link Optional} for a fluent interface
 */
@UtilityClass
public class NBTUtil {

    public Optional<NBTByte> getAsByteTag(NBT tag) {
        return tag.getID().equals(NBTType.TAG_Byte) ? Optional.of((NBTByte) tag) : Optional.empty();
    }

    public Optional<NBTShort> getAsShortTag(NBT tag) {
        return tag.getID().equals(NBTType.TAG_Short) ? Optional.of((NBTShort) tag) : Optional.empty();
    }

    public Optional<NBTInt> getAsIntTag(NBT tag) {
        return tag.getID().equals(NBTType.TAG_Int) ? Optional.of((NBTInt) tag) : Optional.empty();
    }

    public Optional<NBTLong> getAsLongTag(NBT tag) {
        return tag.getID().equals(NBTType.TAG_List) ? Optional.of((NBTLong) tag) : Optional.empty();
    }

    public Optional<NBTFloat> getAsFloatTag(NBT tag) {
        return tag.getID().equals(NBTType.TAG_Float) ? Optional.of((NBTFloat) tag) : Optional.empty();
    }

    public Optional<NBTDouble> getAsDoubleTag(NBT tag) {
        return tag.getID().equals(NBTType.TAG_Double) ? Optional.of((NBTDouble) tag) : Optional.empty();
    }

    public Optional<NBTString> getAsStringTag(NBT tag) {
        return tag.getID().equals(NBTType.TAG_String) ? Optional.of((NBTString) tag) : Optional.empty();
    }

    public Optional<NBTList<?>> getAsListTag(NBT tag) {
        return tag.getID().equals(NBTType.TAG_List) ? Optional.of((NBTList<?>) tag) : Optional.empty();
    }

    public Optional<NBTCompound> getAsCompoundTag(NBT tag) {
        return tag.getID().equals(NBTType.TAG_Compound) ? Optional.of((NBTCompound) tag) : Optional.empty();
    }

    public Optional<NBTIntArray> getAsIntArrayTag(NBT tag) {
        return tag.getID().equals(NBTType.TAG_Int_Array) ? Optional.of((NBTIntArray) tag) : Optional.empty();
    }

    public Optional<NBTLongArray> getAsLongArrayTag(NBT tag) {
        return tag.getID().equals(NBTType.TAG_Long_Array) ? Optional.of((NBTLongArray) tag) : Optional.empty();
    }

    public Optional<NBTByteArray> getAsByteArrayTag(NBT tag) {
        return tag.getID().equals(NBTType.TAG_Byte_Array) ? Optional.of((NBTByteArray) tag) : Optional.empty();
    }

    public Optional<NBTEnd> getAsEndTag(NBT tag) {
        return tag.getID().equals(NBTType.TAG_End) ? Optional.of((NBTEnd) tag) : Optional.empty();
    }

}
