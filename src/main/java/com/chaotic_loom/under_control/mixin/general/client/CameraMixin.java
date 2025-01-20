package com.chaotic_loom.under_control.mixin.general.client;

import com.chaotic_loom.under_control.api.cutscene.CutsceneAPI;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow protected abstract void setPosition(double d, double e, double f);

    @Shadow
    public abstract void setRotation(float f, float g);

    @Shadow private boolean initialized;

    @Shadow private BlockGetter level;

    @Shadow private Entity entity;

    @Shadow private boolean detached;

    @Inject(method = "setup", at = @At("HEAD"), cancellable = true)
    public void setup(BlockGetter blockGetter, Entity entity, boolean bl, boolean bl2, float f, CallbackInfo ci) {
        long currentTime = System.currentTimeMillis();

        if (CutsceneAPI.isPlaying()) {
            this.initialized = true;
            this.level = blockGetter;
            this.entity = entity;
            this.detached = bl;

            Vector3f position = CutsceneAPI.getPosition();
            Vector2f rotation = CutsceneAPI.getRotation();

            this.setPosition(position.x, position.y, position.z);
            this.setRotation(rotation.x, position.y);

            ci.cancel();
        }
    }
}
