package ru.tpsd.eatinganimationmod.mixin;

import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin implements SynchronousResourceReloader {
	@Shadow
	@Final
	private ItemModels models;

	@Shadow
	protected abstract void renderGuiItemModel(MatrixStack matrices, ItemStack stack, int x, int y, BakedModel model);

	@Inject(method = "innerRenderInGui(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V", at = @At("HEAD"), cancellable = true)
	void innerRenderInGui(MatrixStack matrices, LivingEntity entity, World world, ItemStack stack, int x, int y, int seed, int depth, CallbackInfo ci) {
		if (!stack.isEmpty() && stack.isFood()) {
			BakedModel bakedModel = this.models.getModel(stack);
			matrices.push();
			matrices.translate(0.0F, 0.0F, (float) (50 + (bakedModel.hasDepth() ? depth : 0)));
			this.renderGuiItemModel(matrices, stack, x, y, bakedModel);

			matrices.pop();
			ci.cancel();
		}
	}
}