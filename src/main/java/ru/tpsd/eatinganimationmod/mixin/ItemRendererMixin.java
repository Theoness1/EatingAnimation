package ru.tpsd.eatinganimationmod.mixin;


import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.SynchronousResourceReloader;
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
	protected abstract void renderGuiItemModel(ItemStack stack, int x, int y, BakedModel model);

	@Inject(method = "innerRenderInGui(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;II)V", at = @At("HEAD"), cancellable = true)
	void innerRenderInGui(LivingEntity entity, ItemStack stack, int x, int y, CallbackInfo ci) {
		if (!stack.isEmpty() && stack.isFood()) {
			BakedModel bakedModel = this.models.getModel(stack);

			this.renderGuiItemModel(stack, x, y, bakedModel);

			ci.cancel();
		}
	}
}
