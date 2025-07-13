package protosky.mixins.StructureHelperInvokers;

import net.minecraft.structure.OceanRuinGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(OceanRuinGenerator.Piece.class)
public interface OceanRuinGeneratorPieceInvoker {
    @Accessor("integrity")
    float getIntegrity();
}
