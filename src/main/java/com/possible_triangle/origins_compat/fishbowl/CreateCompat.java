package com.possible_triangle.origins_compat.fishbowl;

import com.possible_triangle.origins_compat.OriginsCompat;
import com.possible_triangle.origins_compat.fishbowl.block.WaterBacktank;
import com.possible_triangle.origins_compat.fishbowl.client.WaterTankArmorLayer;
import com.possible_triangle.origins_compat.fishbowl.client.WaterTankOverlay;
import com.possible_triangle.origins_compat.fishbowl.item.WaterBacktankItem;
import com.possible_triangle.origins_compat.fishbowl.tile.WaterBacktankTile;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import com.simibubi.create.content.AllSections;
import com.simibubi.create.content.curiosities.armor.CopperBacktankItem;
import com.simibubi.create.foundation.block.BlockStressDefaults;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.network.chat.Component;
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
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class CreateCompat {

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(OriginsCompat.MOD_ID);

    public static final TagKey<Item> WATER_SOURCE = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(OriginsCompat.MOD_ID,  "water_source"));
    public static final TagKey<Item> ZINC_INGOT = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("forge",  "ingots/zinc"));

    static {
        REGISTRATE.startSection(AllSections.CURIOSITIES);
        REGISTRATE.creativeModeTab(() -> Create.BASE_CREATIVE_TAB);
    }

    public static void init() {
        WaterTankSpoutBehaviour.register();

        var forgeBus = MinecraftForge.EVENT_BUS;
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();

        REGISTRATE.registerEventListeners(modBus);

        forgeBus.addListener(CreateCompat::onTooltip);
        forgeBus.addListener(CreateCompat::onLivingUpdate);

        modBus.addListener(CreateCompat::onAddLayers);
        modBus.addListener(CreateCompat::onClientInit);
    }

    public static void onTooltip(ItemTooltipEvent event) {
        var stack = event.getItemStack();
        var itemTooltip = event.getToolTip();
        var player = event.getPlayer();

        if (player == null) return;
        if (stack.is(CreateCompat.WATER_BACKTANK_ITEM.get()) && TooltipHelper.hasTooltip(stack, player)) {
            List<Component> list = new ArrayList<>();
            list.add(itemTooltip.remove(0));
            TooltipHelper.getTooltip(stack).addInformation(list);
            itemTooltip.addAll(0, list);
        }
    }

    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        WaterTankUtil.onLivingTick(event.getEntityLiving());
    }

    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        var renderers = Stream.of(dispatcher.getSkinMap().values(), dispatcher.renderers.values()).flatMap(Collection::stream);
        renderers.forEach(it -> {
            if (it instanceof LivingEntityRenderer<?, ?> renderer) {
                WaterTankArmorLayer.registerOn(renderer, layer -> renderer.addLayer((RenderLayer) layer));
            }
        });
    }

    public static void onClientInit(FMLClientSetupEvent event) {
        OverlayRegistry.registerOverlayAbove(ForgeIngameGui.AIR_LEVEL_ELEMENT, "Remaining Water", (gui, poseStack, partialTick, width, height) -> {
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

    public static final ItemEntry<CopperBacktankItem.CopperBacktankBlockItem> WATER_BACKTANK_PLACEABLE = REGISTRATE
            .item("water_backtank_placeable", p -> new CopperBacktankItem.CopperBacktankBlockItem(WATER_BACKTANK.get(), p))
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
            .tileEntity("copper_backtank", WaterBacktankTile::new)
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
