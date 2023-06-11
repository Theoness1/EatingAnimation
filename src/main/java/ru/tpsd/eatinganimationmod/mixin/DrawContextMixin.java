package ru.tpsd.eatinganimationmod.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {

	@Shadow @Final private MinecraftClient client;

	@SuppressWarnings(value = "all")
	@ModifyVariable(method = "drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/render/item/ItemRenderer;getModel(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)Lnet/minecraft/client/render/model/BakedModel;"))
	private BakedModel getModel(BakedModel bakedModel, @Nullable LivingEntity entity, @Nullable World world, ItemStack stack, int x, int y, int seed, int z) {
		if(stack.isFood()){
			//return this.getHeldItemModel(stack, null, entity, seed);
		}
		return this.client.getItemRenderer().getModel(stack, world, entity, seed);
	}

	/*public BakedModel getHeldItemModel(ItemStack stack, @Nullable World world, @Nullable LivingEntity entity, int seed) {

		BakedModel bakedModel = client.getItemRenderer().getModels().getModel(stack);

		ClientWorld clientWorld = world instanceof ClientWorld ? (ClientWorld)world : null;
		BakedModel bakedModel1 = stack.isFood()
				? client.getItemRenderer().getModels().getModel(stack)
				: bakedModel.getOverrides().apply(bakedModel, stack, clientWorld, entity, seed);

		return bakedModel1 == null ? client.getItemRenderer().getModels().getModelManager().getMissingModel() : bakedModel1;
	}*/
}