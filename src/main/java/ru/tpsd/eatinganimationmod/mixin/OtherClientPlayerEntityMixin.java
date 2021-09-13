package ru.tpsd.eatinganimationmod.mixin;

import net.minecraft.client.network.OtherClientPlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.tpsd.eatinganimationmod.EatingAnimationClientMod;

@Mixin(OtherClientPlayerEntity.class)
public class OtherClientPlayerEntityMixin {

    OtherClientPlayerEntity otherClientPlayerEntity = ((OtherClientPlayerEntity) (Object) this);
    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo ci){
        if(otherClientPlayerEntity != null && otherClientPlayerEntity.getItemUseTime() > 31) {
            if (EatingAnimationClientMod.a < 31) {

                ++EatingAnimationClientMod.a;

            } else {
                EatingAnimationClientMod.a = 0;
            }
        }
    }
}
