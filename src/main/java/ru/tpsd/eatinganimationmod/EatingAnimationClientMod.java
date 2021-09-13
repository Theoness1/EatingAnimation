package ru.tpsd.eatinganimationmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;

public class EatingAnimationClientMod implements ClientModInitializer {

   public static float a = 0;
    public static final ArrayList<Item> VANILLA_FOOD = new ArrayList<>(Arrays.asList(
            Items.APPLE, Items.BAKED_POTATO, Items.BEEF, Items.BEETROOT, Items.CARROT,
            Items.CHICKEN, Items.BREAD, Items.CHORUS_FRUIT, Items.COD, Items.COOKED_BEEF,
            Items.COOKED_CHICKEN, Items.COOKED_COD, Items.COOKED_MUTTON, Items.COOKED_PORKCHOP, Items.COOKED_RABBIT,
            Items.COOKED_SALMON
    ));


    @Override
    public void onInitializeClient() {

        for (Item item : VANILLA_FOOD) {
            FabricModelPredicateProviderRegistry.register(item, new Identifier("eat"), (itemStack, clientWorld, livingEntity, i) -> {
                if (livingEntity == null) {
                    return 0.0F;
                }

                if(livingEntity instanceof OtherClientPlayerEntity && livingEntity.getItemUseTime() > 31){
return a / 30;
                }
                return livingEntity.getActiveItem() != itemStack ? 0.0F : (itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / 30.0F;
            });

            FabricModelPredicateProviderRegistry.register(item, new Identifier("eating"), (itemStack, clientWorld, livingEntity, i) -> {
                if (livingEntity == null) {
                    return 0.0F;
                }

                return livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
            });
        }
    }
}
