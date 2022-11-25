package com.possible_triangle.origins_compat.fishbowl.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.possible_triangle.origins_compat.fishbowl.CreateCompat;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.function.Consumer;

public class WaterTankArmorLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public WaterTankArmorLayer(RenderLayerParent<T, M> layer) {
        super(layer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, LivingEntity entity, float yaw, float pitch, float partialTicks, float fa, float fb, float fc) {
        if (entity.getPose() == Pose.SLEEPING) return;
        if (!CreateCompat.WATER_BACKTANK_ITEM.get().isWornBy(entity)) return;

        M entityModel = getParentModel();

        if (!(entityModel instanceof HumanoidModel<?> model)) return;

        RenderType renderType = Sheets.cutoutBlockSheet();
        BlockState renderedState = CreateCompat.WATER_BACKTANK.getDefaultState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH);
        SuperByteBuffer backtank = CachedBufferer.block(renderedState);
        SuperByteBuffer cogs = CachedBufferer.partial(AllBlockPartials.COPPER_BACKTANK_COGS, renderedState);

        poseStack.pushPose();

        model.body.translateAndRotate(poseStack);
        poseStack.translate(-1 / 2f, 10 / 16f, 1f);
        poseStack.scale(1, -1, -1);

        backtank.forEntityRender()
                .light(light)
                .renderInto(poseStack, buffer.getBuffer(renderType));

        cogs.centre()
                .rotateY(180)
                .unCentre()
                .translate(0, 6.5f / 16, 11f / 16)
                .rotate(Direction.EAST, AngleHelper.rad(2 * AnimationTickHolder.getRenderTime(entity.level) % 360))
                .translate(0, -6.5f / 16, -11f / 16);

        cogs.forEntityRender()
                .light(light)
                .renderInto(poseStack, buffer.getBuffer(renderType));

        poseStack.popPose();
    }

    public static void registerOn(EntityRenderer<?> entityRenderer, Consumer<RenderLayer<?,?>> register) {
        if (!(entityRenderer instanceof LivingEntityRenderer<?, ?> renderer)) return;
        if (!(renderer.getModel() instanceof HumanoidModel<?>)) return;
        WaterTankArmorLayer<?, ?> layer = new WaterTankArmorLayer<>(renderer);
        register.accept(layer);
    }
}
