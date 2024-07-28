package com.possible_triangle.origins_compat.api;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static com.possible_triangle.origins_compat.Constants.MOD_ID;

public class OriginsCompatTags {

    public static final TagKey<Item> WATER_SOURCE = TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, "water_source"));

}
