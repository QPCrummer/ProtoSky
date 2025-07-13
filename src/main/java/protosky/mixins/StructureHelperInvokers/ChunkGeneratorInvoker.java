package protosky.mixins.StructureHelperInvokers;

import net.minecraft.util.math.BlockBox;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.util.PlacedFeatureIndexer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.function.Supplier;

@Mixin(ChunkGenerator.class)
public interface ChunkGeneratorInvoker {

    @Accessor("indexedFeaturesListSupplier")
    Supplier<List<PlacedFeatureIndexer.IndexedFeatures>> getIndexedFeaturesListSupplier();

    @Invoker("getBlockBoxForChunk")
    static BlockBox getBlockBoxForChunkInvoker(Chunk chunk) {
        throw new AssertionError();
    }


}
