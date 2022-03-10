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
		BakedModel bakedModel4 = stack.isOf(Items.APPLE) || stack.isOf(Items.BAKED_POTATO) || stack.isOf(Items.BEEF) || stack.isOf(Items.BEETROOT) || stack.isOf(Items.BREAD)
				|| stack.isOf(Items.CARROT) || stack.isOf(Items.CHICKEN) || stack.isOf(Items.CHORUS_FRUIT) || stack.isOf(Items.COD) || stack.isOf(Items.COOKED_BEEF)
				|| stack.isOf(Items.COOKED_CHICKEN) || stack.isOf(Items.COOKED_COD) || stack.isOf(Items.COOKED_MUTTON) || stack.isOf(Items.COOKED_PORKCHOP)
				|| stack.isOf(Items.COOKED_RABBIT) || stack.isOf(Items.COOKED_SALMON) || stack.isOf(Items.COOKIE) || stack.isOf(Items.DRIED_KELP) || stack.isOf(Items.GLOW_BERRIES)
				|| stack.isOf(Items.GOLDEN_APPLE) || stack.isOf(Items.ENCHANTED_GOLDEN_APPLE) || stack.isOf(Items.GOLDEN_CARROT) || stack.isOf(Items.HONEY_BOTTLE) || stack.isOf(Items.MELON_SLICE)
				|| stack.isOf(Items.MUTTON) || stack.isOf(Items.POISONOUS_POTATO) || stack.isOf(Items.PORKCHOP) || stack.isOf(Items.POTATO)
				|| stack.isOf(Items.PUMPKIN_PIE) || stack.isOf(Items.RABBIT) || stack.isOf(Items.ROTTEN_FLESH)
				|| stack.isOf(Items.SALMON) || stack.isOf(Items.SPIDER_EYE) || stack.isOf(Items.SWEET_BERRIES) || stack.isOf(Items.TROPICAL_FISH) ?
				this.models.getModel(stack) : bakedModel3.getOverrides().apply(bakedModel3, stack, clientWorld, entity, seed);

		return bakedModel4 == null ? this.models.getModelManager().getMissingModel() : bakedModel4;
	}

	@ModifyVariable(method = "innerRenderInGui(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;IIII)V", at = @At("STORE"), ordinal = 0)
	private BakedModel injected(BakedModel bakedModel,@Nullable LivingEntity entity, ItemStack itemStack,int seed) {
		if(itemStack.isFood()) {
			return bakedModel = this.getHeldItemModel_1(itemStack, (World)null, entity, seed);
		}
			return this.getModel(itemStack, (World)null, entity, seed);

	}
}
