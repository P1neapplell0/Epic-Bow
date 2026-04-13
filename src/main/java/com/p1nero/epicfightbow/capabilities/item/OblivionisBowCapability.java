package com.p1nero.epicfightbow.capabilities.item;

import com.p1nero.epicfightbow.gameassets.EFBowAnimations;
import com.p1nero.epicfightbow.gameassets.EFBowSkills;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import org.jetbrains.annotations.Nullable;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.registry.entries.EpicFightParticles;
import yesman.epicfight.registry.entries.EpicFightSounds;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.RangedWeaponCapability;
import yesman.epicfight.world.capabilities.item.Style;

import java.util.List;

public class OblivionisBowCapability extends RangedWeaponCapability {
    private final List<AnimationManager.AnimationAccessor<? extends AttackAnimation>> attackMotionBow;
    private final List<AnimationManager.AnimationAccessor<? extends AttackAnimation>> attackMotionDagger;
    private final List<AnimationManager.AnimationAccessor<? extends AttackAnimation>> mountAttackMotion;

    public OblivionisBowCapability(RangedWeaponCapability.Builder builder) {
        super(builder);
        this.attackMotionBow = List.of(EFBowAnimations.BOW_AUTO1, EFBowAnimations.BOW_AUTO2, EFBowAnimations.BOW_AUTO3, EFBowAnimations.BOW_DASH_ATTACK, EFBowAnimations.BOW_JUMP_ATTACK);
        this.attackMotionDagger = List.of(Animations.DAGGER_DUAL_AUTO1, Animations.DAGGER_DUAL_AUTO2, Animations.DAGGER_DUAL_AUTO3, Animations.DAGGER_DUAL_DASH, Animations.DAGGER_DUAL_AIR_SLASH);
        this.mountAttackMotion = List.of(Animations.SWORD_MOUNT_ATTACK);
    }

    @Override
    public Collider getWeaponCollider() {
        return ColliderPreset.LONGSWORD;
    }

    public Style getStyle(LivingEntityPatch<?> entityPatch) {

        return Styles.TWO_HAND;
    }

    public SoundEvent getHitSound() {
        return EpicFightSounds.BLUNT_HIT.get();
    }

    public HitParticleType getHitParticle() {
        return EpicFightParticles.HIT_BLUNT.get();
    }

    public List<AnimationManager.AnimationAccessor<? extends AttackAnimation>> getAutoAttackMotion(PlayerPatch<?> playerpatch) {
        return this.getStyle(playerpatch) == Styles.TWO_HAND ? this.attackMotionDagger : this.attackMotionBow;
    }

    public List<AnimationManager.AnimationAccessor<? extends AttackAnimation>> getMountAttackMotion() {
        return this.mountAttackMotion;
    }

    @Override
    public LivingMotion getLivingMotion(LivingEntityPatch<?> entityPatch, InteractionHand hand) {
        return entityPatch.getOriginal().isUsingItem() && entityPatch.getOriginal().getUseItem().getUseAnimation() == UseAnim.BOW ? LivingMotions.AIM : null;
    }

    @Override
    public @Nullable Skill getInnateSkill(PlayerPatch<?> playerpatch, ItemStack itemstack) {
        return EFBowSkills.MORTIS_INNATE.get();
    }
}
