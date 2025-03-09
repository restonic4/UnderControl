package com.chaotic_loom.under_control.mixin.general.client;

import com.chaotic_loom.under_control.api.cutscene.CutsceneAPI;
import com.chaotic_loom.under_control.client.rendering.screen_shake.types.ScreenShakeGlobalManager;
import com.chaotic_loom.under_control.util.RandomHelper;
import net.minecraft.client.Camera;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow protected abstract void setPosition(double d, double e, double f);

    @Shadow
    protected abstract void setRotation(float f, float g);

    @Shadow protected abstract void move(double d, double e, double f);

    @Shadow public abstract float getXRot();

    @Shadow public abstract float getYRot();

    @Shadow @Final private Quaternionf rotation;

    @Shadow private float eyeHeight;
    @Shadow private float eyeHeightOld;
    @Shadow private float yRot;
    @Shadow private float xRot;

    @Shadow protected abstract double getMaxZoom(double d);

    @Inject(method = "setup", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Camera;detached:Z", shift = At.Shift.AFTER), cancellable = true)
    public void setup(BlockGetter blockGetter, Entity entity, boolean thirdPerson, boolean front, float interpolation, CallbackInfo ci) {
        if (CutsceneAPI.isPlaying()) {
            Vector3f position = CutsceneAPI.getPosition();
            Vector2f rotation = CutsceneAPI.getRotation();

            if (position != null) {
                this.setPosition(position.x, position.y, position.z);
            } else {
                applyVanillaPosition(entity, thirdPerson, front, interpolation);
            }

            if (rotation != null) {
                this.setRotation(rotation.x, rotation.y);
                entity.setXRot(rotation.y);
                entity.setYRot(rotation.x);
            } else {
                applyVanillaRotation(entity, thirdPerson, front, interpolation);
            }

            shake();

            ci.cancel();
        }
    }

    @Inject(method = "setup", at = @At("TAIL"))
    public void setupEnd(BlockGetter blockGetter, Entity entity, boolean bl, boolean bl2, float f, CallbackInfo ci) {
        shake();
    }

    @Unique
    private void shake() {
        float screenShakeIntensity = ScreenShakeGlobalManager.computeGlobalShakeOffset();

        this.move(RandomHelper.randomBetween(screenShakeIntensity), RandomHelper.randomBetween(screenShakeIntensity), RandomHelper.randomBetween(screenShakeIntensity));
        this.setRotation(this.getYRot() + RandomHelper.randomBetween(screenShakeIntensity), this.getXRot() + RandomHelper.randomBetween(screenShakeIntensity));
    }

    @Unique
    private void applyVanillaPosition(Entity entity, boolean thirdPerson, boolean front, float interpolation) {
        this.setPosition(Mth.lerp((double)interpolation, entity.xo, entity.getX()), Mth.lerp((double)interpolation, entity.yo, entity.getY()) + (double)Mth.lerp(interpolation, this.eyeHeightOld, this.eyeHeight), Mth.lerp((double)interpolation, entity.zo, entity.getZ()));

        if (thirdPerson) {
            this.move(-this.getMaxZoom((double)4.0F), (double)0.0F, (double)0.0F);
        }
    }

    @Unique
    private void applyVanillaRotation(Entity entity, boolean thirdPerson, boolean front, float interpolation) {
        if (thirdPerson) {
            if (front) {
                this.setRotation(this.yRot + 180.0F, -this.xRot);
            }
        } else if (entity instanceof LivingEntity && ((LivingEntity)entity).isSleeping()) {
            Direction direction = ((LivingEntity)entity).getBedOrientation();
            this.setRotation(direction != null ? direction.toYRot() - 180.0F : 0.0F, 0.0F);
        }
    }
}
