package protosky.mixins;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(ServerWorld.class)
public abstract class LightRodFixMixin extends World {
    protected LightRodFixMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @ModifyArg(method = "getLightningRodPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/poi/PointOfInterestStorage;getNearestPosition(Ljava/util/function/Predicate;Ljava/util/function/Predicate;Lnet/minecraft/util/math/BlockPos;ILnet/minecraft/world/poi/PointOfInterestStorage$OccupationStatus;)Ljava/util/Optional;"), index = 1)
    private Predicate<BlockPos> modifyHeightmapType(Predicate<BlockPos> original) {
        return innerPos -> innerPos.getY() == this.getTopY(Heightmap.Type.MOTION_BLOCKING, innerPos.getX(), innerPos.getZ()) - 1;
    }
}
