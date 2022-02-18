package ru.tpsd.eatinganimationmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;

public class DrinkingAnimationClientMod implements ClientModInitializer {

    public static float a = 0;

    public static final ArrayList<Item> VANILLA_DRINK = new ArrayList<>(Collections.singletonList(
            Items.POTION
    ));

    @Override
    public void onInitializeClient() {

        for(Item item : VANILLA_DRINK) {
            FabricModelPredicateProviderRegistry.register(item, new Identifier("drink"), (itemStack, clientWorld, livingEntity, i) -> {
                if (livingEntity == null) {
                    return 0.0F;
                }

                if(livingEntity instanceof OtherClientPlayerEntity && livingEntity.getItemUseTime() > 31){
                    return a / 30;
                }
                return livingEntity.getActiveItem() != itemStack ? 0.0F : (itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / 30.0F;
            });

            FabricModelPredicateProviderRegistry.register(item, new Identifier("drinking"), (itemStack, clientWorld, livingEntity, i) -> {
                if (livingEntity == null) {
                    return 0.0F;
                }

                return livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
            });
        }
    }

}