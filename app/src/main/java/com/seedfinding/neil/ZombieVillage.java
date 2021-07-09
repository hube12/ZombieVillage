package com.seedfinding.neil;

import kaptainwutax.biomeutils.source.OverworldBiomeSource;
import kaptainwutax.featureutils.structure.Village;
import kaptainwutax.mcutils.rand.ChunkRand;
import kaptainwutax.mcutils.rand.seed.StructureSeed;
import kaptainwutax.mcutils.util.pos.BPos;
import kaptainwutax.mcutils.util.pos.CPos;
import kaptainwutax.mcutils.util.pos.RPos;
import kaptainwutax.mcutils.version.MCVersion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

public class ZombieVillage {
    public static final MCVersion VERSION = MCVersion.v1_17;

    public static void main(String[] args) {
        LongStream.rangeClosed(0, 1L << 48).sequential().forEach(ZombieVillage::run);
    }

    public static void run(long structureSeed) {
        ChunkRand rand = new ChunkRand();
        Village village = new Village(VERSION);
        RPos minBound = new BPos(-3000, 0, -3000).toRegionPos(village.getSpacing() * 16);
        RPos maxBound = new BPos(3000, 0, 3000).toRegionPos(village.getSpacing() * 16);
        int nbr = 0;
        for (int regX = minBound.getX() + 1; regX <= maxBound.getX(); regX++) {
            for (int regZ = minBound.getZ() + 1; regZ <= maxBound.getZ(); regZ++) {
                CPos pos = village.getInRegion(structureSeed, regX, regZ, rand);
                rand.setCarverSeed(structureSeed, pos.getX(), pos.getZ(), VERSION);
                // burn a call (nextInt)
                rand.advance(1);
                long seed = rand.getSeed();
                int i = 0;
                i += rand.nextInt(204) >= 200 ? 1 : 0;
                rand.setSeed(seed, false);
                i += rand.nextInt(250) >= 245 ? 1 : 0;
                rand.setSeed(seed, false);
                i += rand.nextInt(459) >= 450 ? 1 : 0;
                rand.setSeed(seed, false);
                i += rand.nextInt(100) >= 98 ? 1 : 0;
                rand.setSeed(seed, false);
                i += rand.nextInt(306) >= 300 ? 1 : 0;
                if (i > 0) {
                    nbr += 1;
                }
            }
        }
        if (nbr > 15) {
            System.out.println(nbr + ": Potential " + structureSeed);
            fullCheck(structureSeed);
        }
    }


    public static void fullCheck(long structureSeed) {
        long start=System.nanoTime();
        StructureSeed.getWorldSeeds(structureSeed).asStream().boxed().forEach(ws -> {
            ChunkRand rand = new ChunkRand();
            Village village = new Village(VERSION);
            RPos minBound = new BPos(-3000, 0, -3000).toRegionPos(village.getSpacing() * 16);
            RPos maxBound = new BPos(3000, 0, 3000).toRegionPos(village.getSpacing() * 16);
            List<CPos> posList = new ArrayList<>();
            OverworldBiomeSource biomeSource = new OverworldBiomeSource(VERSION, ws);
            for (int regX = minBound.getX() + 1; regX <= maxBound.getX(); regX++) {
                for (int regZ = minBound.getZ() + 1; regZ <= maxBound.getZ(); regZ++) {
                    CPos pos = village.getInRegion(structureSeed, regX, regZ, rand);
                    if (village.canSpawn(pos, biomeSource) && village.isZombieVillage(structureSeed, pos, rand)) {
                        posList.add(pos);
                    }
                }
            }
            if (posList.size() > 9) {
                System.out.printf("Found %d zombie village for world seed %d%n", posList.size(), ws);
                for (CPos pos : posList) {
                    BPos bPos = pos.toBlockPos();
                    System.out.printf("\t/tp @p %d ~ %d%n", bPos.getX(), bPos.getZ());
                }
            }
        });
        System.out.println(System.nanoTime()-start);
    }
}
