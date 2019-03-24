package alexiil.mc.lib.attributes.fluid.volume;

import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.EmptyFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

/** {@link ItemStack} equivalent for {@link Fluid fluids}. */
public class NormalFluidVolume extends FluidVolume {

    public NormalFluidVolume(NormalFluidKey fluid, int amount) {
        super(fluid, amount);
        if (fluid.fluid instanceof EmptyFluid && fluid != FluidKeys.EMPTY) {
            throw new IllegalArgumentException("Different empty fluid!");
        }
        if (fluid.fluid instanceof BaseFluid && fluid.fluid != ((BaseFluid) fluid.fluid).getStill()) {
            throw new IllegalArgumentException("Only the still version of fluids are allowed!");
        }
    }

    public NormalFluidVolume(NormalFluidKey fluid, CompoundTag tag) {
        super(fluid, tag);
        if (fluid == null) {
            throw new NullPointerException("fluid");
        }
        if (fluid.fluid instanceof EmptyFluid && fluid != FluidKeys.EMPTY) {
            throw new IllegalArgumentException("Different empty fluid!");
        }
        if (fluid.fluid instanceof BaseFluid && fluid.fluid != ((BaseFluid) fluid.fluid).getStill()) {
            throw new IllegalArgumentException("Only the still version of fluids are allowed!");
        }
    }

    public final Fluid getRawFluid() {
        return getFluidKey().fluid;
    }

    @Override
    public NormalFluidKey getFluidKey() {
        return (NormalFluidKey) fluidKey;
    }
}
