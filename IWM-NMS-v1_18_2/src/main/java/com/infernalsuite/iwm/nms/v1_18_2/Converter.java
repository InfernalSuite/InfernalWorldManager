package com.infernalsuite.iwm.nms.v1_18_2;

import com.infernalsuite.iwm.api.utils.NBTLibTypes;
import com.infernalsuite.iwm.api.utils.NibbleArray;
import lombok.experimental.UtilityClass;
import net.minecraft.nbt.*;
import net.minecraft.world.level.chunk.DataLayer;
import org.jglrxavpok.hephaistos.nbt.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class Converter {

    DataLayer convertArray(NibbleArray array) {
        return new DataLayer(array.getBackingArray());
    }

    NibbleArray convertArray(DataLayer dataLayer) {
        return dataLayer == null ? null : new NibbleArray(dataLayer.getData());
    }

    Tag convertToTag(NBT nbt) {
        try {
            switch (NBTLibTypes.getEnumRep(nbt.getID())) {
                case TAG_BYTE -> { return ByteTag.valueOf(((NBTByte) nbt).getValue()); }
                case TAG_SHORT -> { return ShortTag.valueOf(((NBTShort) nbt).getValue()); }
                case TAG_INT -> { return IntTag.valueOf(((NBTInt) nbt).getValue()); }
                case TAG_LONG -> { return LongTag.valueOf(((NBTLong) nbt).getValue()); }
                case TAG_FLOAT -> { return FloatTag.valueOf(((NBTFloat) nbt).getValue()); }
                case TAG_DOUBLE -> { return DoubleTag.valueOf(((NBTDouble) nbt).getValue()); }
                case TAG_BYTE_ARRAY -> { return new ByteArrayTag(((NBTByteArray) nbt).getValue().copyArray()); }
                case TAG_STRING -> { return StringTag.valueOf(((NBTString) nbt).getValue()); }
                case TAG_LIST -> {
                    ListTag listTag = new ListTag();
                    ((NBTList<?>) nbt).asListView().stream().map(Converter::convertToTag).forEach(listTag::add);
                    return listTag;
                }
                case TAG_COMPOUND -> {
                    CompoundTag compoundTag = new CompoundTag();
                    ((NBTCompound) nbt).forEach((key, value) -> compoundTag.put(key, convertToTag(value)));
                    return compoundTag;
                }
                case TAG_INT_ARRAY -> { return new IntArrayTag(((NBTIntArray) nbt).getValue().copyArray()); }
                case TAG_LONG_ARRAY -> { return new LongArrayTag(((NBTLongArray) nbt).getValue().copyArray()); }
                default -> throw new IllegalArgumentException("Invalid tag type: " + nbt.getID().getReadableName());
            }
        } catch (Exception ex) {
            // TODO: Log exception
            throw ex;
        }
    }

    // The explicit cast here actually saves some heap allocation!
    @SuppressWarnings("RedundantCast")
    NBT convertToNBT(Tag tag) {
        switch (tag.getId()) {
            case Tag.TAG_BYTE -> { return new NBTByte(((ByteTag) tag).getAsByte()); }
            case Tag.TAG_SHORT -> { return new NBTShort(((ShortTag) tag).getAsShort()); }
            case Tag.TAG_INT -> { return new NBTInt(((IntTag) tag).getAsInt()); }
            case Tag.TAG_LONG -> { return new NBTLong(((LongTag) tag).getAsLong()); }
            case Tag.TAG_FLOAT -> { return new NBTFloat(((FloatTag) tag).getAsFloat()); }
            case Tag.TAG_DOUBLE -> { return new NBTDouble(((DoubleTag) tag).getAsDouble()); }
            case Tag.TAG_BYTE_ARRAY -> { return new NBTByteArray(((ByteArrayTag) tag).getAsByteArray()); }
            case Tag.TAG_STRING -> { return new NBTString(((StringTag) tag).getAsString()); }
            case Tag.TAG_LIST -> {
                ListTag listTag = (ListTag) tag;
                List<NBT> list = new ArrayList<>(listTag.size());
                listTag.stream().map(Converter::convertToNBT).forEach(list::add);
                return new NBTList<>(NBTType.byIndex(listTag.getElementType()), list);
            }
            case Tag.TAG_COMPOUND -> {
                CompoundTag compoundTag = (CompoundTag) tag;
                Map<String, NBT> compoundMap = new HashMap<>();
                // We could get an NPE here if 'compoundTag.get(key)' returns null, but if it does, that's likely a far bigger issue...
                //noinspection ConstantConditions
                compoundTag.getAllKeys().forEach(key -> compoundMap.put(key, convertToNBT(compoundTag.get(key))));
                return new NBTCompound(compoundMap);
            }
            case Tag.TAG_INT_ARRAY -> { return new NBTIntArray(((IntArrayTag) tag).getAsIntArray()); }
            case Tag.TAG_LONG_ARRAY -> { return new NBTLongArray(((LongArrayTag) tag).getAsLongArray()); }
            default -> throw new IllegalArgumentException("Invalid tag type: " + tag.getId());
        }
    }

}
