package ru.tpsd.eatinganimationmod.mixin;


import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin implements SynchronousResourceReloader {
	@Shadow
	@Final
	private ItemModels models;

	@Shadow public abstract BakedModel getModel(ItemStack stack, @Nullable World world, @Nullable LivingEntity entity, int seed);

	public BakedModel getHeldItemModel_1(ItemStack stack, @Nullable World world, @Nullable LivingEntity entity, int seed) {

		BakedModel bakedModel3 = this.models.getModel(stack);

		ClientWorld clientWorld = world instanceof ClientWorld ? (ClientWorld)world : null;
		BakedModel bakedModel4 = stack.isFood()
			? this.models.getModel(stack)
			: bakedModel3.getOverrides().apply(bakedModel3, stack, clientWorld, entity, seed);

		return bakedModel4 == null ? this.models.getModelManager().getMissingModel() : bakedModel4;
	}

	@ModifyVariable(method = "innerRenderInGui(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;IIII)V", at = @At("STORE"), ordinal = 0)
	private BakedModel injected(BakedModel bakedModel,@Nullable LivingEntity entity, ItemStack itemStack,int seed) {
		if(itemStack.isFood()){
			return this.getHeldItemModel_1(itemStack, null, entity, seed);
		}
		return getModel(itemStack, null, entity, seed);
	}
}
