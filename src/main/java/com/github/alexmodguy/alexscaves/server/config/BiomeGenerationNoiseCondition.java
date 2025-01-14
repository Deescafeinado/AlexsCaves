package com.github.alexmodguy.alexscaves.server.config;

import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRarity;
import com.github.alexthe666.citadel.server.event.EventReplaceBiome;
import net.minecraft.core.QuartPos;

import java.util.ArrayList;
import java.util.List;

public class BiomeGenerationNoiseCondition {

    private boolean disabledCompletely;
    private int distanceFromSpawn;
    private final int alexscavesRarityOffset;
    private final float[] continentalness;
    private final float[] erosion;
    private final float[] humidity;
    private final float[] temperature;
    private final float[] weirdness;
    private final float[] depth;
    private final List<String> dimensions;

    private BiomeGenerationNoiseCondition(boolean disabledCompletely, int distanceFromSpawn, int alexscavesRarityOffset, float[] continentalness, float[] erosion, float[] humidity, float[] temperature, float[] weirdness, float[] depth, String[] dimensions) {
        this.disabledCompletely = disabledCompletely;
        this.distanceFromSpawn = distanceFromSpawn;
        this.continentalness = continentalness;
        this.erosion = erosion;
        this.humidity = humidity;
        this.temperature = temperature;
        this.weirdness = weirdness;
        this.depth = depth;
        this.alexscavesRarityOffset = alexscavesRarityOffset;
        this.dimensions = List.of(dimensions);
    }

    public boolean test(EventReplaceBiome event) {
        if (disabledCompletely) {
            return false;
        }
        if (!isFarEnoughFromSpawn(event, distanceFromSpawn)) {
            return false;
        }
        if (!ACBiomeRarity.testBiomeRarity(event.getWorldSeed(), alexscavesRarityOffset, event.getX(), event.getZ())) {
            return false;
        }
        if (continentalness != null && continentalness.length >= 2 && !event.testContinentalness(continentalness[0], continentalness[1])) {
            return false;
        }
        if (erosion != null && erosion.length >= 2 && !event.testErosion(erosion[0], erosion[1])) {
            return false;
        }
        if (humidity != null && humidity.length >= 2 && !event.testHumidity(humidity[0], humidity[1])) {
            return false;
        }
        if (temperature != null && temperature.length >= 2 && !event.testTemperature(temperature[0], temperature[1])) {
            return false;
        }
        if (weirdness != null && weirdness.length >= 2 && !event.testWeirdness(weirdness[0], weirdness[1])) {
            return false;
        }
        if (depth != null && depth.length >= 2 && !event.testDepth(depth[0], depth[1])) {
            return false;
        }
        if(event.getWorldDimension() != null && !dimensions.contains(event.getWorldDimension().location().toString())){
            return false;
        }
        return true;
    }

    private static boolean isFarEnoughFromSpawn(EventReplaceBiome event, double dist) {
        int x = QuartPos.fromSection(event.getX());
        int z = QuartPos.toBlock(event.getZ());
        return x * x + z * z >= dist * dist;
    }

    public boolean isDisabledCompletely() {
        return disabledCompletely;
    }

    public boolean isInvalid() {
        return dimensions == null && !disabledCompletely;
    }

    public static final class Builder {
        private boolean disabledCompletely;
        private int distanceFromSpawn;
        private float[] alexBiomeRarity;
        private float[] continentalness;
        private float[] erosion;
        private float[] humidity;
        private float[] temperature;
        private float[] weirdness;
        private float[] depth;
        private String[] dimensions;
        private int rarityOffset;

        public Builder() {
        }

        public Builder disabledCompletely(boolean disabledCompletely) {
            this.disabledCompletely = disabledCompletely;
            return this;
        }

        public Builder alexscavesRarityOffset(int rarityOffset) {
            this.rarityOffset = rarityOffset;
            return this;
        }

        public Builder distanceFromSpawn(int distanceFromSpawn) {
            this.distanceFromSpawn = distanceFromSpawn;
            return this;
        }

        public Builder continentalness(float... continentalness) {
            this.continentalness = continentalness;
            return this;
        }

        public Builder erosion(float... erosion) {
            this.erosion = erosion;
            return this;
        }

        public Builder humidity(float... humidity) {
            this.humidity = humidity;
            return this;
        }

        public Builder temperature(float... temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder weirdness(float... weirdness) {
            this.weirdness = weirdness;
            return this;
        }

        public Builder depth(float... depth) {
            this.depth = depth;
            return this;
        }

        public Builder dimensions(String... dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        public BiomeGenerationNoiseCondition build() {
            return new BiomeGenerationNoiseCondition(disabledCompletely, distanceFromSpawn, rarityOffset, continentalness, erosion, humidity, temperature, weirdness, depth, dimensions);
        }
    }
}
