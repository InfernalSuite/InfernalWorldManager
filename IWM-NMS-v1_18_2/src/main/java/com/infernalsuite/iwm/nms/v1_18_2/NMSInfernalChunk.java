package com.infernalsuite.iwm.nms.v1_18_2;

import com.infernalsuite.iwm.api.utils.NibbleArray;
import com.infernalsuite.iwm.api.world.InfernalChunk;
import com.infernalsuite.iwm.api.world.InfernalChunkSection;
import com.infernalsuite.iwm.common.nms.CraftInfernalChunkSection;
import com.mojang.serialization.Codec;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.storage.ChunkSerializer;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import net.minecraft.world.level.lighting.LevelLightEngine;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTLongArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class NMSInfernalChunk implements InfernalChunk {

    private LevelChunk chunk;

    @Override
    public @NonNull String getWorldName() {
        return chunk.getLevel().getMinecraftWorld().serverLevelData.getLevelName();
    }

    @Override
    public int getX() {
        return chunk.getPos().x;
    }

    @Override
    public int getZ() {
        return chunk.getPos().z;
    }

    @Override
    public InfernalChunkSection[] getSections() {
        InfernalChunkSection[] sections = new InfernalChunkSection[this.chunk.getMaxSection() - this.chunk.getMinSection() - 1];
        LevelLightEngine lightEngine = chunk.getLevel().getChunkSource().getLightEngine();

        Registry<Biome> biomeRegistry = chunk.getLevel().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY);

        Codec<PalettedContainer<Holder<Biome>>> codec = PalettedContainer.codec(biomeRegistry.asHolderIdMap(), biomeRegistry.holderByNameCodec(),
                PalettedContainer.Strategy.SECTION_BIOMES, biomeRegistry.getHolderOrThrow(Biomes.PLAINS));

        for (int sectionId = 0; sectionId < chunk.getSections().length; sectionId++) {
            LevelChunkSection section = chunk.getSections()[sectionId];

            NibbleArray blockLightArray = Converter.convertArray(lightEngine.getLayerListener(LightLayer.BLOCK).getDataLayerData(SectionPos.of(chunk.getPos(), sectionId)));

            NibbleArray skyLightArray = Converter.convertArray(lightEngine.getLayerListener(LightLayer.SKY).getDataLayerData(SectionPos.of(chunk.getPos(), sectionId)));

            Tag blockStateData = ChunkSerializer.BLOCK_STATE_CODEC.encodeStart(NbtOps.INSTANCE, section.getStates()).getOrThrow(false, System.err::println);
            Tag biomeData = codec.encodeStart(NbtOps.INSTANCE, section.getBiomes()).getOrThrow(false, System.err::println);

            NBTCompound blockStateCompound = (NBTCompound) Converter.convertToNBT(blockStateData);
            NBTCompound biomeCompound = (NBTCompound) Converter.convertToNBT(biomeData);

            sections[sectionId] = new CraftInfernalChunkSection(null, null, blockStateCompound, biomeCompound, blockLightArray, skyLightArray);
        }

        return sections;
    }

    @Override
    public int getMinSection() {
        return this.chunk.getMinSection();
    }

    @Override
    public int getMaxSection() {
        return this.chunk.getMaxSection();
    }

    @Override
    public int[] getBiomes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull NBTCompound getHeightMaps() {
        Map<String, NBTLongArray> heightMaps = new HashMap<>();
        chunk.getHeightmaps().stream()
                .filter(e -> e.getKey().keepAfterWorldgen())
                .forEach(e -> heightMaps.put(e.getKey().name(), new NBTLongArray(e.getValue().getRawData())));
        return new NBTCompound(heightMaps);
    }

    @Override
    public @NonNull List<NBTCompound> getTileEntities() {
        List<NBTCompound> tileEntities = new ArrayList<>();

        chunk.blockEntities.values().stream()
                .map(BlockEntity::saveWithFullMetadata)
                .forEach(e -> tileEntities.add((NBTCompound) Converter.convertToNBT(e)));

        return tileEntities;
    }

    @Override
    public @NonNull List<NBTCompound> getEntities() {
        List<NBTCompound> entities = new ArrayList<>();

        PersistentEntitySectionManager<Entity> entityManager = chunk.level.entityManager;

        entityManager.getEntities(this.chunk.getPos()).forEach(e -> {
            CompoundTag entityNbt = new CompoundTag();
            if (e.save(entityNbt)) {
                entities.add((NBTCompound) Converter.convertToNBT(entityNbt));
            }
        });

        return entities;
    }
}
