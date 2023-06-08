package ru.tpsd.eatinganimationmod.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.world.World;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public abstract class DrawContextLegacyMixin implements SynchronousResourceReloader {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    @Final
    private MatrixStack matrices;

    @Shadow
    @Final
    private VertexConsumerProvider.Immediate vertexConsumers;

    @Shadow
    public void draw() {
        RenderSystem.disableDepthTest();
        this.vertexConsumers.draw();
        RenderSystem.enableDepthTest();
    }

    @Shadow
    public VertexConsumerProvider.Immediate getVertexConsumers() {
        return this.vertexConsumers;
    }

    @Inject(method = "drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V", at = @At("HEAD"), cancellable = true)
    void innerRenderInGui(LivingEntity entity, World world, ItemStack stack, int x, int y, int seed, int z, CallbackInfo ci) {
        if (!stack.isEmpty() && stack.isFood()) {
            BakedModel bakedModel = this.client.getItemRenderer().getModels().getModel(stack);
            this.matrices.push();
            this.matrices.translate((float)(x + 8), (float)(y + 8), (float)(150 + (bakedModel.hasDepth() ? z : 0)));
            this.matrices.multiplyPositionMatrix(new Matrix4f().scaling(1.0F, -1.0F, 1.0F));
            this.matrices.scale(16.0F, 16.0F, 16.0F);

            boolean bl = !bakedModel.isSideLit();
            if (bl) {
                    DiffuseLighting.disableGuiDepthLighting();
            }

            this.client
                    .getItemRenderer()
                    .renderItem(stack, ModelTransformationMode.GUI, false, this.matrices, this.getVertexConsumers(), 15728880, OverlayTexture.DEFAULT_UV, bakedModel);
            this.draw();
            if (bl) {
                    DiffuseLighting.enableGuiDepthLighting();
            }

            matrices.pop();
            ci.cancel();
        }
    }
}