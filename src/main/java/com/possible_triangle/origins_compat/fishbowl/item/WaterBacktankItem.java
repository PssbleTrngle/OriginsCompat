package com.possible_triangle.origins_compat.fishbowl.item;

import com.simibubi.create.content.curiosities.armor.CopperBacktankItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.ItemStack;

public class WaterBacktankItem extends CopperBacktankItem {

    public static final int WATER_BAR_COLOR = 0x1979FF;

    public WaterBacktankItem(Properties properties, ItemEntry<CopperBacktankBlockItem> placeable) {
        super(properties, placeable);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return WATER_BAR_COLOR;
    }

}
