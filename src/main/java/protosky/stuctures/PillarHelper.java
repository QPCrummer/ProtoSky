package protosky.stuctures;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PaneBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.EndSpikeFeature;

import java.util.Random;

public class PillarHelper {

    //Copied from stack overflow
    //https://stackoverflow.com/questions/401847/circle-rectangle-collision-detection-intersection
    //This checks if a spike intersects with a chunk properly.
    private static boolean spikeChunkIntersects(EndSpikeFeature.Spike circle, Chunk rect) {
        int rectXBlocks = (rect.getPos().x * 16) + 8;
        int rectYBlocks = (rect.getPos().z * 16) + 8;

        int circleDistanceX = Math.abs(circle.getCenterX() - rectXBlocks);
        int circleDistanceY = Math.abs(circle.getCenterZ() - rectYBlocks);

        int radiusWithMargin = 8 + circle.getRadius();

        if (circleDistanceX > radiusWithMargin) { return false; }
        if (circleDistanceY > radiusWithMargin) { return false; }

        if (circleDistanceX <= 8) { return true; }
        if (circleDistanceY <= 8) { return true; }

        //int cornerDistanceSq = (circleDistanceX - 8)^2 + (circleDistanceY - 8)^2;
        int cornerDistanceSq = (circleDistanceX - 8) * (circleDistanceX - 8) +
                (circleDistanceY - 8) * (circleDistanceY - 8);

        return (cornerDistanceSq <= (circle.getRadius()^2));
    }

    public static void generate(StructureWorldAccess world, Chunk chunk) {
        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        //-6 Chunks is 96 blocks, all pillars should be within this
        //spike.isInChunk() Doesn't work because it checks the center of the pillar cutting it off.
        if (chunkX >= -6 && chunkX <= 6 && chunkZ >= -6 && chunkZ <= 6) {
            for (EndSpikeFeature.Spike spike : EndSpikeFeature.getSpikes(world)) {
                if(spikeChunkIntersects(spike, chunk)) PillarHelper.generateSpike(world, spike, chunk);
            }
        }
    }

    public static void generateSpike(ServerWorldAccess world, EndSpikeFeature.Spike spike, Chunk chunk) {
        int i = spike.getRadius();
        for (BlockPos blockPos : BlockPos.iterate(
                new BlockPos(spike.getCenterX() - i, 0, spike.getCenterZ() - i),
                new BlockPos(spike.getCenterX() + i, spike.getHeight() + 10, spike.getCenterZ() + i)
        )) {
            if (blockPos.getSquaredDistance(spike.getCenterX(), blockPos.getY(), spike.getCenterZ()) <= (double) (i * i + 1) && blockPos.getY() < spike.getHeight()) {
                StructureHelper.setBlockInChunk(chunk, blockPos, Blocks.OBSIDIAN.getDefaultState());
            } else if (blockPos.getY() > 65) {
                StructureHelper.setBlockInChunk(chunk, blockPos, Blocks.AIR.getDefaultState());
            }

        }
        if (spike.isGuarded()) {
            BlockPos.Mutable mutable = new BlockPos.Mutable();

            for (int m = -2; m <= 2; ++m) {
                for (int n = -2; n <= 2; ++n) {
                    for (int o = 0; o <= 3; ++o) {
                        boolean bl = MathHelper.abs(m) == 2;
                        boolean bl2 = MathHelper.abs(n) == 2;
                        boolean bl3 = o == 3;
                        if (bl || bl2 || bl3) {
                            boolean bl4 = m == -2 || m == 2 || bl3;
                            boolean bl5 = n == -2 || n == 2 || bl3;
                            BlockState blockState = Blocks.IRON_BARS
                                    .getDefaultState()
                                    .with(PaneBlock.NORTH, bl4 && n != -2)
                                    .with(PaneBlock.SOUTH, bl4 && n != 2)
                                    .with(PaneBlock.WEST, bl5 && m != -2)
                                    .with(PaneBlock.EAST, bl5 && m != 2);
                            StructureHelper.setBlockInChunk(chunk, mutable.set(spike.getCenterX() + m, spike.getHeight() + o, spike.getCenterZ() + n), blockState);
                        }
                    }
                }
            }
        }

        //Check if where we want to put the bedrock is in the chunk we're in.
        if(Math.abs(((chunk.getPos().x * 16) + 8) - spike.getCenterX()) <= 8 && Math.abs(((chunk.getPos().z * 16) + 8) - spike.getCenterZ()) <= 8) {
            EndCrystalEntity endCrystalEntity = EntityType.END_CRYSTAL.create(world.toServerWorld(), SpawnReason.CHUNK_GENERATION);
            endCrystalEntity.refreshPositionAndAngles(
                    (double)spike.getCenterX() + 0.5, spike.getHeight() + 1, (double)spike.getCenterZ() + 0.5, new Random().nextFloat() * 360.0F, 0.0F
            );
            world.spawnEntity(endCrystalEntity);
            StructureHelper.setBlockInChunk(chunk, new BlockPos(spike.getCenterX(), spike.getHeight(), spike.getCenterZ()), Blocks.BEDROCK.getDefaultState());
        }
    }
}