/*
 * Copyright (c) 2019 AlexIIL
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package alexiil.mc.lib.attributes.fluid.impl;

import alexiil.mc.lib.attributes.ListenerRemovalToken;
import alexiil.mc.lib.attributes.ListenerToken;
import alexiil.mc.lib.attributes.fluid.FixedFluidInvView;
import alexiil.mc.lib.attributes.fluid.FluidInvTankChangeListener;

/** A sub-view of an existing {@link FixedFluidInvView}. */
public class SubFixedFluidInvView extends AbstractPartialFixedFluidInvView {

    /** The tanks that we use. */
    private final int fromIndex, toIndex;

    public SubFixedFluidInvView(FixedFluidInvView inv, int fromIndex, int toIndex) {
        super(inv);
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException(
                "fromIndex was greater than toIndex! (" + fromIndex + " > " + toIndex + ")");
        }
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
    }

    /** @return The tank that the internal {@link #inv} should use. */
    @Override
    protected int getInternalTank(int tank) {
        tank += fromIndex;
        if (tank >= toIndex) {
            throw new IllegalArgumentException("The given tank " + (tank - fromIndex)
                + "is greater than the size of this inventory! (" + getTankCount() + ")");
        }
        return tank;
    }

    @Override
    public int getTankCount() {
        return toIndex - fromIndex;
    }

    @Override
    public FixedFluidInvView getFixedView() {
        if (getClass() == SubFixedFluidInvView.class) {
            return this;
        }
        return super.getFixedView();
    }

    @Override
    public ListenerToken addListener(FluidInvTankChangeListener listener, ListenerRemovalToken removalToken) {
        FixedFluidInvView wrapper = this;
        return inv.addListener((realInv, tank, previous, current) -> {
            assert realInv == inv;
            if (tank >= fromIndex && tank < toIndex) {
                int exposedTank = tank - fromIndex;
                listener.onChange(wrapper, exposedTank, previous, current);
            }
        }, removalToken);
    }
}
