package ru.tpsd.eatinganimationmod;

import java.util.ArrayList;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class EatingAnimationClientMod implements ClientModInitializer {

   public static float itemUseTime = 0;
   private static final ArrayList<Item> foodItems = new ArrayList<>(Registries.ITEM.stream().filter(item -> item.getComponents().contains(DataComponentTypes.FOOD)).toList());
   static {
       foodItems.add(Items.MILK_BUCKET);
   }

   // asbyth thank you bru! ඞ

    @Override
    public void onInitializeClient() {
        for (Item item : foodItems) {
            ModelPredicateProviderRegistry.register(item, new Identifier("eat"), (itemStack, clientWorld, livingEntity, i) -> {
                if (livingEntity == null) {
                    return 0.0F;
                }

                if(livingEntity instanceof OtherClientPlayerEntity && livingEntity.getItemUseTime() > 31){
                    return itemUseTime / 30;
                }
                return livingEntity.getActiveItem() != itemStack ? 0.0F : (itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / 30.0F;
            });

            ModelPredicateProviderRegistry.register(item, new Identifier("eating"), (itemStack, clientWorld, livingEntity, i) -> {
                if (livingEntity == null) {
                    return 0.0F;
                }

                return livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
            });

        }

        FabricLoader.getInstance().getModContainer("eatinganimationid").ifPresent(eatinganimation ->
                ResourceManagerHelper.registerBuiltinResourcePack(EatingAnimationClientMod.locate("supporteatinganimation"), eatinganimation, ResourcePackActivationType.DEFAULT_ENABLED));
    }

    public static Identifier locate(String path) {
        return new Identifier(path);
    }
}
