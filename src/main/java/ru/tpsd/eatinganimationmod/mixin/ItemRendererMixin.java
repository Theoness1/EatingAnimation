package ru.tpsd.eatinganimationmod.mixin;


import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

	public BakedModel getHeldItemModel_1(ItemStack stack, @Nullable World world, @Nullable LivingEntity entity, int seed) {

		BakedModel bakedModel3 = this.models.getModel(stack);

		ClientWorld clientWorld = world instanceof ClientWorld ? (ClientWorld)world : null;
		BakedModel bakedModel4 =
				stack.isOf(Items.APPLE) || stack.isOf(Items.BAKED_POTATO) || stack.isOf(Items.BEEF) || stack.isOf(Items.BEETROOT) || stack.isOf(Items.BREAD)
				|| stack.isOf(Items.CARROT) || stack.isOf(Items.CHICKEN) || stack.isOf(Items.CHORUS_FRUIT) || stack.isOf(Items.COD) || stack.isOf(Items.COOKED_BEEF)
				|| stack.isOf(Items.COOKED_CHICKEN) || stack.isOf(Items.COOKED_COD) || stack.isOf(Items.COOKED_MUTTON) || stack.isOf(Items.COOKED_PORKCHOP)
						|| stack.isOf(Items.COOKED_RABBIT) || stack.isOf(Items.COOKED_SALMON)?
				this.models.getModel(stack) : bakedModel3.getOverrides().apply(bakedModel3, stack, clientWorld, entity, seed);

		return bakedModel4 == null ? this.models.getModelManager().getMissingModel() : bakedModel4;
	}

	@ModifyVariable(method = "innerRenderInGui(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;IIII)V", at = @At("STORE"), ordinal = 0)
	private BakedModel injected(BakedModel bakedModel,@Nullable LivingEntity entity, ItemStack itemStack,int seed) {
		return bakedModel = this.getHeldItemModel_1(itemStack, (World)null, entity, seed);
	}
}
