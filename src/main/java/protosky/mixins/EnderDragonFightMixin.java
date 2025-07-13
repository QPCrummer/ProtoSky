package protosky.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnderDragonFight.class)
public class EnderDragonFightMixin {
    @WrapOperation(method = "generateEndPortal", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;withY(I)Lnet/minecraft/util/math/BlockPos;"))
    private BlockPos adjustExitPortalLocation(BlockPos instance, int y, Operation<BlockPos> original) {
        return original.call(instance, 2);
    }
}
