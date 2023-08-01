package com.possible_triangle.origins_compat;

import com.possible_triangle.origins_compat.api.OriginsCompatTags;
import com.possible_triangle.origins_compat.block.WaterBacktank;
import com.possible_triangle.origins_compat.block.tile.WaterBacktankTile;
import com.possible_triangle.origins_compat.item.WaterBacktankItem;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.equipment.armor.BacktankItem;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class CommonCreateCompat {

    private static final TagKey<Item> ZINC_INGOT = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("forge", "ingots/zinc"));

    private static BlockEntry<WaterBacktank> WATER_BACKTANK_BLOCK;
    private static ItemEntry<BacktankItem.BacktankBlockItem> WATER_BACKTANK_PLACEABLE;

    private static ItemEntry<WaterBacktankItem> WATER_BACKTANK_ITEM;

    private static BlockEntityEntry<WaterBacktankTile> WATER_BACKTANK_TILE;

    public static BlockEntityType<WaterBacktankTile> getWaterBacktankBlockEntity() {
        return WATER_BACKTANK_TILE.get();
    }

    public static WaterBacktankItem getWaterBacktankItem() {
        return WATER_BACKTANK_ITEM.get();
    }

    public static void register(CreateRegistrate registrate) {
        registrate.creativeModeTab(() -> AllCreativeModeTabs.BASE_CREATIVE_TAB)
                .setTooltipModifierFactory(it ->
                        new ItemDescription.Modifier(it, TooltipHelper.Palette.STANDARD_CREATE)
                                .andThen(TooltipModifier.mapNull(KineticStats.create(it)))
                );

        WATER_BACKTANK_BLOCK = registrate
                .block("water_backtank", WaterBacktank::new)
                .initialProperties(SharedProperties::softMetal)
                .blockstate((c, p) -> p.horizontalBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
                .transform(TagGen.pickaxeOnly())
                .addLayer(() -> RenderType::cutoutMipped)
                .transform(BlockStressDefaults.setImpact(4.0))
                .loot(CommonCreateCompat::backTankLoot)
                .register();

        WATER_BACKTANK_ITEM = registrate.item("water_backtank", p -> new WaterBacktankItem(p, WATER_BACKTANK_PLACEABLE))
                .model(AssetLookup.customGenericItemModel("_", "item"))
                .tag(OriginsCompatTags.WATER_SOURCE)
                .recipe((c, p) -> new ShapedRecipeBuilder(c.get(), 1)
                        .pattern("APA")
                        .pattern("IBI")
                        .pattern(" I ")
                        .define('I', ZINC_INGOT)
                        .define('A', AllItems.ANDESITE_ALLOY.get())
                        .define('P', AllBlocks.FLUID_PIPE.get())
                        .define('B', AllBlocks.ZINC_BLOCK.get())
                        .unlockedBy("has_zinc", RegistrateRecipeProvider.has(ZINC_INGOT))
                        .save(p)
                )
                .register();

        WATER_BACKTANK_PLACEABLE = registrate
                .item("water_backtank_placeable", p -> new BacktankItem.BacktankBlockItem(WATER_BACKTANK_BLOCK.get(), WATER_BACKTANK_ITEM::get, p))
                .model((c, p) -> p.withExistingParent(c.getName(), p.mcLoc("item/barrier")))
                .register();

        WATER_BACKTANK_TILE = registrate
                .blockEntity("copper_backtank", WaterBacktankTile::new)
                //.instance(() -> CopperBacktankInstance::new)
                .validBlocks(WATER_BACKTANK_BLOCK)
                //.renderer(() -> CopperBacktankRenderer::new)
                .register();
    }

    private static void backTankLoot(RegistrateBlockLootTables lt, WaterBacktank block) {
        LootTable.Builder builder = LootTable.lootTable();
        LootItemCondition.Builder survivesExplosion = ExplosionCondition.survivesExplosion();
        lt.add(block, builder.withPool(LootPool.lootPool()
                .when(survivesExplosion)
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(WATER_BACKTANK_ITEM.get())
                        .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                        .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                .copy("Water", "Water"))
                        .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                .copy("Enchantments", "Enchantments")))));
    }

}
