package com.infernalsuite.iwm.api.utils;

import lombok.Getter;
import org.jglrxavpok.hephaistos.nbt.NBTType;

public enum NBTLibTypes {

    TAG_BYTE(NBTType.TAG_Byte),
    TAG_SHORT(NBTType.TAG_Short),
    TAG_INT(NBTType.TAG_Int),
    TAG_LONG(NBTType.TAG_Long),
    TAG_FLOAT(NBTType.TAG_Float),
    TAG_DOUBLE(NBTType.TAG_Double),
    TAG_STRING(NBTType.TAG_String),
    TAG_LIST(NBTType.TAG_List),
    TAG_COMPOUND(NBTType.TAG_Compound),
    TAG_INT_ARRAY(NBTType.TAG_Int_Array),
    TAG_LONG_ARRAY(NBTType.TAG_Long_Array),
    TAG_BYTE_ARRAY(NBTType.TAG_Byte_Array),
    TAG_END(NBTType.TAG_End);

    NBTLibTypes(NBTType<?> type) {
        this.type = type;
    }

    @Getter
    private final NBTType<?> type;

    public static NBTLibTypes getEnumRep(NBTType<?> type) {
        if (NBTType.TAG_Byte.equals(type)) return TAG_BYTE;
        else if (NBTType.TAG_Short.equals(type)) return TAG_SHORT;
        else if (NBTType.TAG_Int.equals(type)) return TAG_INT;
        else if (NBTType.TAG_Long.equals(type)) return TAG_LONG;
        else if (NBTType.TAG_Float.equals(type)) return TAG_FLOAT;
        else if (NBTType.TAG_Double.equals(type)) return TAG_DOUBLE;
        else if (NBTType.TAG_String.equals(type)) return TAG_STRING;
        else if (NBTType.TAG_List.equals(type)) return TAG_LIST;
        else if (NBTType.TAG_Compound.equals(type)) return TAG_COMPOUND;
        else if (NBTType.TAG_Int_Array.equals(type)) return TAG_INT_ARRAY;
        else if (NBTType.TAG_Long_Array.equals(type)) return TAG_LONG_ARRAY;
        else if (NBTType.TAG_Byte_Array.equals(type)) return TAG_BYTE_ARRAY;
        else if (NBTType.TAG_End.equals(type)) return TAG_END;
        else throw new IllegalArgumentException("Invalid tag type: " + type.getReadableName());
    }

}
