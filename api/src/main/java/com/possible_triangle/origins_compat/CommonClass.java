package com.possible_triangle.origins_compat;

import com.possible_triangle.origins_compat.api.OriginsCompatTags;
import com.possible_triangle.origins_compat.api.WaterTankSources;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CommonClass {

    public static void init() {
        WaterTankSources.addSupplier(entity -> {
            List<ItemStack> stacks = new ArrayList<>();
            for (ItemStack stack : entity.getArmorSlots())
                if (stack.is(OriginsCompatTags.WATER_SOURCE)) stacks.add(stack);

            return stacks;
        });
    }

}
