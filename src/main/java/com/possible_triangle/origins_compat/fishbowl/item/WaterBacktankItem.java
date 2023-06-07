package com.possible_triangle.origins_compat.fishbowl.item;

import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.armor.AllArmorMaterials;
import com.simibubi.create.content.equipment.armor.BacktankItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.ItemStack;

public class WaterBacktankItem extends BacktankItem {

    public static final int WATER_BAR_COLOR = 0x1979FF;

    public WaterBacktankItem(Properties properties, ItemEntry<BacktankBlockItem> placeable) {
        super(AllArmorMaterials.COPPER, properties, Create.asResource("copper_diving"), placeable);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return WATER_BAR_COLOR;
    }

}
