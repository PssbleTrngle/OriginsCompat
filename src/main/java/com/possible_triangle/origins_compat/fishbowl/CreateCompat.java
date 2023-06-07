package com.possible_triangle.origins_compat.fishbowl;

import com.possible_triangle.origins_compat.OriginsCompat;
import com.possible_triangle.origins_compat.fishbowl.block.WaterBacktank;
import com.possible_triangle.origins_compat.fishbowl.client.WaterTankOverlay;
import com.possible_triangle.origins_compat.fishbowl.item.WaterBacktankItem;
import com.possible_triangle.origins_compat.fishbowl.tile.WaterBacktankTile;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.equipment.armor.BacktankItem.BacktankBlockItem;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
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
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class CreateCompat {

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(OriginsCompat.MOD_ID)
            .creativeModeTab(() -> AllCreativeModeTabs.BASE_CREATIVE_TAB)
            .setTooltipModifierFactory(it ->
                    new ItemDescription.Modifier(it, TooltipHelper.Palette.STANDARD_CREATE)
                            .andThen(TooltipModifier.mapNull(KineticStats.create(it)))
            );

    public static final TagKey<Item> WATER_SOURCE = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(OriginsCompat.MOD_ID, "water_source"));
    public static final TagKey<Item> ZINC_INGOT = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("forge", "ingots/zinc"));

    public static void init() {
        WaterTankSpoutBehaviour.register();

        var forgeBus = MinecraftForge.EVENT_BUS;
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();

        REGISTRATE.registerEventListeners(modBus);

        forgeBus.addListener(CreateCompat::onLivingUpdate);

        modBus.addListener(CreateCompat::registerOverlays);
    }

    public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
        WaterTankUtil.onLivingTick(event.getEntity());
    }

    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.AIR_LEVEL.id(), "remaining_water", (gui, poseStack, partialTick, width, height) -> {
            WaterTankOverlay.renderRemainingWaterOverlay(poseStack, width, height);
        });
    }

    public static final BlockEntry<WaterBacktank> WATER_BACKTANK = REGISTRATE
            .block("water_backtank", WaterBacktank::new)
            .initialProperties(SharedProperties::softMetal)
            .blockstate((c, p) -> p.horizontalBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
            .transform(TagGen.pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(BlockStressDefaults.setImpact(4.0))
            .loot(CreateCompat::backTankLoot)
            .register();

    public static final ItemEntry<BacktankBlockItem> WATER_BACKTANK_PLACEABLE = REGISTRATE
            .item("water_backtank_placeable", p -> new BacktankBlockItem(WATER_BACKTANK.get(), CreateCompat.WATER_BACKTANK_ITEM::get, p))
            .model((c, p) -> p.withExistingParent(c.getName(), p.mcLoc("item/barrier")))
            .register();

    public static final ItemEntry<WaterBacktankItem> WATER_BACKTANK_ITEM = REGISTRATE.item("water_backtank", p -> new WaterBacktankItem(p, WATER_BACKTANK_PLACEABLE))
            .model(AssetLookup.customGenericItemModel("_", "item"))
            .tag(WATER_SOURCE)
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

    public static final BlockEntityEntry<WaterBacktankTile> WATER_BACKTANK_TILE = REGISTRATE
            .blockEntity("copper_backtank", WaterBacktankTile::new)
            //.instance(() -> CopperBacktankInstance::new)
            .validBlocks(WATER_BACKTANK)
            //.renderer(() -> CopperBacktankRenderer::new)
            .register();

    private static void backTankLoot(RegistrateBlockLootTables lt, WaterBacktank block) {
        LootTable.Builder builder = LootTable.lootTable();
        LootItemCondition.Builder survivesExplosion = ExplosionCondition.survivesExplosion();
        lt.add(block, builder.withPool(LootPool.lootPool()
                .when(survivesExplosion)
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(WATER_BACKTANK_ITEM.get())
                        .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                        .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                .copy("Air", "Air"))
                        .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                .copy("Enchantments", "Enchantments")))));
    }

}
