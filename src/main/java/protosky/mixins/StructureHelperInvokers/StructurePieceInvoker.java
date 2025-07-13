package protosky.mixins.StructureHelperInvokers;

import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StructurePiece.class)
public interface StructurePieceInvoker {

    @Accessor("boundingBox")
    void setBoundingBox(BlockBox boundingBox);

    @Accessor("boundingBox")
    BlockBox getBoundingBox();
}
