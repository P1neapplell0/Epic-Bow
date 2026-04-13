package com.p1nero.epicfightbow.gameassets;

import com.p1nero.epicfightbow.EpicFightBowMod;
import com.p1nero.epicfightbow.animations.ScanAttackAnimation;
import com.p1nero.epicfightbow.item.EFBowItemState;
import com.p1nero.epicfightbow.mob_effect.EFBowEffects;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.EventHooks;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.MovementAnimation;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.damagesource.StunType;

@EventBusSubscriber(modid = EpicFightBowMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class EFBowAnimations {
    public static AnimationManager.AnimationAccessor<MovementAnimation> BOW_RUN;
    public static AnimationManager.AnimationAccessor<ScanAttackAnimation> BOW_AUTO1;
    public static AnimationManager.AnimationAccessor<ScanAttackAnimation> BOW_AUTO2;
    public static AnimationManager.AnimationAccessor<ScanAttackAnimation> BOW_AUTO3;
    public static AnimationManager.AnimationAccessor<AttackAnimation> BOW_DASH_ATTACK;
    public static AnimationManager.AnimationAccessor<ScanAttackAnimation> BOW_JUMP_ATTACK;
    public static AnimationManager.AnimationAccessor<AttackAnimation> ELBOW_2;
    public static AnimationManager.AnimationAccessor<AttackAnimation> ELBOW_3;

    @SubscribeEvent
    public static void efb$registerAnimations(AnimationManager.AnimationRegistryEvent event) {
        event.newBuilder(EpicFightBowMod.MOD_ID, EFBowAnimations::buildBowAnimations);
    }

    private static void buildBowAnimations(AnimationManager.AnimationBuilder builder) {
        BOW_RUN = builder.nextAccessor("biped/bow_run", accessor -> new MovementAnimation(true, accessor, Armatures.BIPED)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, ((dynamicAnimation, livingEntityPatch, v, v1, v2) -> BOW_RUN.get().getPlaySpeed(livingEntityPatch, dynamicAnimation) * 2.0F)));
        BOW_AUTO1 = builder.nextAccessor("biped/bow_auto1", accessor ->
                new ScanAttackAnimation(0.15F, 0, 0.15F, 65 / 60F, 65 / 60F,
                    InteractionHand.MAIN_HAND, EFBowColliders.BOW_SCAN, Armatures.BIPED.get().rootJoint, accessor, Armatures.BIPED)
                        .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                        .addEvents(setFullBowUseTime(20 / 60F), shootIn(30 / 60F),
                                setFullBowUseTime(50 / 60F), shootIn(60 / 60F)));
        BOW_AUTO2 = builder.nextAccessor("biped/bow_auto2", accessor ->
                new ScanAttackAnimation(0.15F, 0, 0.15F, 65 / 60F, 65 / 60F,
                        InteractionHand.MAIN_HAND, EFBowColliders.BOW_SCAN, Armatures.BIPED.get().rootJoint, accessor, Armatures.BIPED)
                        .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                        .addEvents(setFullBowUseTime(20 / 60F), shootIn(30 / 60F),
                                setFullBowUseTime(50 / 60F), shootIn(60 / 60F)));
        BOW_AUTO3 = builder.nextAccessor("biped/bow_auto3", accessor ->
                new ScanAttackAnimation(0.15F, 0, 0.15F, 100 / 60F, 120 / 60F,
                        InteractionHand.MAIN_HAND, EFBowColliders.BOW_SCAN, Armatures.BIPED.get().rootJoint, accessor, Armatures.BIPED)
                        .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                        .addEvents(setFullBowUseTime(50 / 60F), shootIn(80/60F),
                                setFullBowUseTime(85 / 60F), shootIn(90/60F),
                                setFullBowUseTime(95 / 60F), shootIn(100/60F)));
        BOW_DASH_ATTACK = builder.nextAccessor("biped/bow_dash_attack", accessor ->
                new AttackAnimation(0.15F, 0, 0, 40 / 60F, 60 / 60F,
                    EFBowColliders.BOW_DASH, Armatures.BIPED.get().rootJoint, accessor, Armatures.BIPED)
                        .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.KNOCKDOWN)
                        .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.0f))
                        .addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, true));
        BOW_JUMP_ATTACK = builder.nextAccessor("biped/bow_jump_attack", accessor ->
                new ScanAttackAnimation(0.15F, 0, 0.15F, 20 / 60F, 80 / 60F,
                        InteractionHand.MAIN_HAND, EFBowColliders.BOW_SCAN, Armatures.BIPED.get().rootJoint, accessor, Armatures.BIPED)
                        .addEvents(setFullBowUseTime(10 / 60F), shootIn(15 / 60F),
                                setFullBowUseTime(16 / 60F), shootIn(20 / 60F)));

        ELBOW_2 = builder.nextAccessor("biped/elbow_2", accessor ->
                new AttackAnimation(0.15F, 20 / 60F, 20 / 60F, 40 / 60F, 50 / 60F,
                        EFBowColliders.BOW_ELBOW, Armatures.BIPED.get().rootJoint, accessor, Armatures.BIPED)
                        .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.FALL)
                        .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.2F))
                        .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(8.0F))
                        .addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, true));

        ELBOW_3 = builder.nextAccessor("biped/elbow_3", accessor ->
                new AttackAnimation(0.15F, 20 / 60F, 20 / 60F, 40 / 60F, 50 / 60F,
                        EFBowColliders.BOW_ELBOW, Armatures.BIPED.get().handR, accessor, Armatures.BIPED)
                        .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.KNOCKDOWN)
                        .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.0F))
                        .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(8.0F)));

    }

    public static AnimationEvent.InTimeEvent<?> setFullBowUseTime(float time) {
        return AnimationEvent.InTimeEvent.create(time, (livingEntityPatch, assetAccessor, animationParameters) -> {
            EFBowItemState.setFull(livingEntityPatch.getOriginal().getMainHandItem(), true);
        }, AnimationEvent.Side.CLIENT);
    }
    /**
     * modify from {@link BowItem#releaseUsing(ItemStack, Level, LivingEntity, int)}
     */
    public static AnimationEvent.InTimeEvent<?> shootIn(float time) {
        return AnimationEvent.InTimeEvent.create(time, ((livingEntityPatch, assetAccessor, animationParameters) -> {
            int loopTime = livingEntityPatch.getOriginal().hasEffect(EFBowEffects.DOUBLE_ARROW) ? 2 : 1;
            for(int i = 0; i < loopTime; i++) {
                shootOnce(livingEntityPatch, 3.0F + i * 0.3F);
            }
            livingEntityPatch.getOriginal().stopUsingItem();
        }), AnimationEvent.Side.BOTH);
    }

    private static void shootOnce(LivingEntityPatch<?> livingEntityPatch) {
        shootOnce(livingEntityPatch, 3.0F);
    }

    private static void shootOnce(LivingEntityPatch<?> livingEntityPatch, float speed) {
        LivingEntity living = livingEntityPatch.getOriginal();
        ItemStack itemStack = living.getMainHandItem();
        EFBowItemState.setFull(itemStack, false);
        Level level = living.level();
        if (!(living instanceof ServerPlayer player) || !(itemStack.getItem() instanceof BowItem bowItem)) {
            return;
        }

        ItemStack ammoStack = player.getProjectile(itemStack);
        boolean hasInfiniteAmmo = hasInfiniteAmmo(player, itemStack);
        if (ammoStack.isEmpty() && !hasInfiniteAmmo) {
            return;
        }

        int charge = EventHooks.onArrowLoose(itemStack, level, player, 20, !ammoStack.isEmpty());
        if (charge < 0) {
            return;
        }

        float power = BowItem.getPowerForTime(charge);
        if (power < 0.1F) {
            return;
        }

        LivingEntity target = ScanAttackAnimation.getTarget(livingEntityPatch);
        ItemStack ammoToFire = ammoStack.isEmpty() ? new ItemStack(Items.ARROW) : ammoStack;
        if (!level.isClientSide()) {
            shootArrow(player, bowItem, itemStack, ammoToFire, target, speed * power, power);
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS,
                1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + power * 0.5F);
        damageBow(player, itemStack);
        if (shouldConsumeAmmo(player, itemStack, ammoToFire)) {
            ammoToFire.shrink(1);
            if (ammoToFire.isEmpty()) {
                player.getInventory().removeItem(ammoToFire);
            }
        }
        player.awardStat(Stats.ITEM_USED.get(bowItem));
    }

    private static boolean hasInfiniteAmmo(ServerPlayer player, ItemStack weaponStack) {
        if (player.hasInfiniteMaterials()) {
            return true;
        }

        HolderLookup.RegistryLookup<Enchantment> enchantments = player.serverLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        return EnchantmentHelper.getItemEnchantmentLevel(enchantments.getOrThrow(Enchantments.INFINITY), weaponStack) > 0;
    }

    private static boolean shouldConsumeAmmo(ServerPlayer player, ItemStack weaponStack, ItemStack ammoStack) {
        if (player.hasInfiniteMaterials()) {
            return false;
        }
        return !(ammoStack.is(Items.ARROW) && hasInfiniteAmmo(player, weaponStack));
    }

    private static void shootArrow(ServerPlayer player, BowItem bowItem, ItemStack weaponStack, ItemStack ammoStack,
                                   LivingEntity target, float speed, float power) {
        ArrowItem arrowItem = ammoStack.getItem() instanceof ArrowItem arrow ? arrow : (ArrowItem) Items.ARROW;
        AbstractArrow abstractArrow = arrowItem.createArrow(player.level(), ammoStack, player, weaponStack);

        Vec3 baseAimDirection = target == null ? getPlayerAimDirection(player) : getTargetAimDirection(player, target);
        Vec3 spawnPos = getArrowSpawnPosition(player, baseAimDirection);
        Vec3 shotDirection = target == null ? baseAimDirection.normalize() : target.getEyePosition().subtract(spawnPos).normalize();

        abstractArrow.setPos(spawnPos);
        abstractArrow.shoot(shotDirection.x, shotDirection.y, shotDirection.z, speed, 0.0F);
        if (power == 1.0F) {
            abstractArrow.setCritArrow(true);
        }
        if (!shouldConsumeAmmo(player, weaponStack, ammoStack)) {
            abstractArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }

        player.level().addFreshEntity(abstractArrow);
    }

    private static Vec3 getPlayerAimDirection(ServerPlayer player) {
        return Vec3.directionFromRotation(player.getViewXRot(1.0F), player.getViewYRot(1.0F));
    }

    private static Vec3 getTargetAimDirection(ServerPlayer player, LivingEntity target) {
        return target.getEyePosition().subtract(player.getEyePosition()).normalize();
    }

    private static Vec3 getArrowSpawnPosition(ServerPlayer player, Vec3 aimDirection) {
        Vec3 normalizedDirection = aimDirection.normalize();
        return player.getEyePosition().add(normalizedDirection.scale(0.45D)).add(0.0D, -0.15D, 0.0D);
    }

    private static void damageBow(ServerPlayer player, ItemStack weaponStack) {
        weaponStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(InteractionHand.MAIN_HAND));
    }

    public static Vec3 getJointWorldPos(LivingEntityPatch<?> entityPatch, Joint joint) {
        LivingEntity entity = entityPatch.getOriginal();
        OpenMatrix4f transformMatrix = entityPatch.getArmature().getBoundTransformFor(entityPatch.getAnimator().getPose(0.1f), joint);
        OpenMatrix4f rotation = new OpenMatrix4f().rotate(-(float) Math.toRadians(entity.yBodyRotO + 180.0F), new Vec3f(0.0F, 1.0F, 0.0F));
        OpenMatrix4f.mul(rotation, transformMatrix, transformMatrix);
        return new Vec3(transformMatrix.m30 + (float) entity.getX(), transformMatrix.m31 + (float) entity.getY(), transformMatrix.m32 + (float) entity.getZ());
    }

}
