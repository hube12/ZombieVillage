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

public class CheckSeed {
    public static final MCVersion VERSION=MCVersion.v1_17;
    public static void main(String[] args) {
        ChunkRand rand = new ChunkRand();
        long ws=8574693528901255174L;
        Village village = new Village(VERSION);
        RPos minBound = new BPos(-3000, 0, -3000).toRegionPos(village.getSpacing() * 16);
        RPos maxBound = new BPos(3000, 0, 3000).toRegionPos(village.getSpacing() * 16);
        List<CPos> posList=new ArrayList<>();
        OverworldBiomeSource biomeSource=new OverworldBiomeSource(VERSION,ws);
        for (int regX = minBound.getX() + 1; regX <= maxBound.getX(); regX++) {
            for(int regZ = minBound.getZ() + 1; regZ <= maxBound.getZ(); regZ++) {
                CPos pos = village.getInRegion(ws, regX, regZ, rand);
                if (village.canSpawn(pos,biomeSource) && village.isZombieVillage(ws,pos,rand)){
                    posList.add(pos);
                }
            }
        }
        if (posList.size()>5){
            System.out.printf("Found %d zombie village for world seed %d%n",posList.size(),ws);
            for(CPos pos:posList){
                BPos bPos=pos.toBlockPos();
                System.out.printf("\t/tp @p %d ~ %d%n",bPos.getX(),bPos.getZ());
            }
        }
    }
}
