/*
 * Copyright (c) 2019 AlexIIL
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package alexiil.mc.lib.attributes.fluid.impl;

import java.util.ArrayList;
import java.util.List;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.FluidInsertable;
import alexiil.mc.lib.attributes.fluid.FluidVolumeUtil;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.filter.AggregateFluidFilter;
import alexiil.mc.lib.attributes.fluid.filter.FluidFilter;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;

public final class CombinedFluidInsertable implements FluidInsertable {

    private final List<? extends FluidInsertable> insertables;

    public CombinedFluidInsertable(List<? extends FluidInsertable> list) {
        this.insertables = list;
    }

    @Override
    public FluidVolume attemptInsertion(FluidVolume stack, Simulation simulation) {
        for (FluidInsertable insertable : insertables) {
            stack = insertable.attemptInsertion(stack, simulation);
            if (stack.isEmpty()) {
                return FluidVolumeUtil.EMPTY;
            }
        }
        return stack;
    }

    @Override
    public FluidFilter getInsertionFilter() {
        List<FluidFilter> filters = new ArrayList<>(insertables.size());
        for (int i = 0; i < insertables.size(); i++) {
            filters.add(insertables.get(i).getInsertionFilter());
        }
        return AggregateFluidFilter.anyOf(filters);
    }

    @Override
    public FluidAmount getMinimumAcceptedAmount() {
        FluidAmount fa = null;
        for (FluidInsertable fi : insertables) {
            FluidAmount fa2 = fi.getMinimumAcceptedAmount();
            if (fa2 == null) {
                return null;
            }
            if (fa == null) {
                fa = fa2;
            } else {
                fa = fa.min(fa2);
            }
        }
        return fa;
    }
}
