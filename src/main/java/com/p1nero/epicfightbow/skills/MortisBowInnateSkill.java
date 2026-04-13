package com.p1nero.epicfightbow.skills;

import com.p1nero.epicfightbow.EpicFightBowMod;
import com.p1nero.epicfightbow.gameassets.EFBowAnimations;
import com.p1nero.epicfightbow.mob_effect.EFBowEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.MainFrameAnimation;
import yesman.epicfight.api.event.EntityEventListener;
import yesman.epicfight.api.event.EpicFightEventHooks;
import yesman.epicfight.api.event.types.entity.DealDamageEvent;
import yesman.epicfight.api.event.types.player.ModifyComboCounter;
import yesman.epicfight.client.input.EpicFightKeyMappings;
import yesman.epicfight.registry.entries.EpicFightSkillDataKeys;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.common.ComboAttacks;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MortisBowInnateSkill extends WeaponInnateSkill {
    protected int consumeSpeed;
    protected int effectDuration;

    public MortisBowInnateSkill(WeaponInnateSkill.Builder<?> builder) {
        super(builder);
    }

    @Override
    public void loadDatapackParameters(CompoundTag parameters) {
        super.loadDatapackParameters(parameters);
        this.maxStackSize = 1;
        this.consumeSpeed = parameters.getInt("consume_speed");
        this.effectDuration = parameters.getInt("effect_duration");
    }

    @Override
    public void onInitiate(SkillContainer container, EntityEventListener listener) {
        super.onInitiate(container, listener);
        listener.registerEvent(EpicFightEventHooks.Entity.DELIVER_DAMAGE_POST, (DealDamageEvent.Post event) -> {
            if (event.getDamageSource().isDirect()) {
                if (event.getDamageSource().getAnimation() == EFBowAnimations.ELBOW_2) {
                    event.getTarget().setDeltaMovement(event.getTarget().getDeltaMovement().add(0.0D, 0.5D * event.getDamageSource().getBaseImpact(), 0.0D));
                }

                if (event.getDamageSource().getAnimation() == EFBowAnimations.ELBOW_3) {
                    Vec3 dir = event.getTarget().position().subtract(container.getExecutor().getOriginal().position()).normalize();
                    event.getTarget().setDeltaMovement(event.getTarget().getDeltaMovement().add(dir));
                }

                return;
            }

            PlayerPatch<?> playerPatch = container.getExecutor();
            ItemStack mainHandItem = playerPatch.getOriginal().getMainHandItem();
            CapabilityItem capabilityItem = EpicFightCapabilities.getItemStackCapability(mainHandItem);
            if (capabilityItem == null || !(capabilityItem.getInnateSkill(playerPatch, mainHandItem) instanceof MortisBowInnateSkill)) {
                return;
            }

            if (!container.isFull()) {
                float value = container.getResource() + event.getModifiedDamage();
                if (value > 0.0F) {
                    this.setConsumptionSynchronize(container, value * 2.0F);
                }
            }
        }, this);
    }

    @Override
    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
        container.getExecutor().getEventListener().removeListenersBelongTo(this);
    }

    @Override
    public void updateContainer(SkillContainer container) {
        super.updateContainer(container);
        PlayerPatch<?> playerPatch = container.getExecutor();
        if (container.isFull()) {
            if (!playerPatch.isLogicalClient()) {
                playerPatch.getOriginal().addEffect(new MobEffectInstance(EFBowEffects.DOUBLE_ARROW, effectDuration, 0, false, false, true));
                this.setConsumptionSynchronize(container, 0.0F);
            }
        } else if (!playerPatch.isLogicalClient()) {
            this.setConsumptionSynchronize(container, container.getResource() - consumeSpeed);
        }
    }

    @Override
    public void executeOnServer(SkillContainer container, CompoundTag args) {
        SkillContainer basicAttackContainer = container.getExecutor().getSkill(SkillSlots.COMBO_ATTACKS);
        SkillDataManager manager = basicAttackContainer.getDataManager();
        int comboCounter = manager.getDataValue(EpicFightSkillDataKeys.COMBO_COUNTER);
        if (comboCounter == 1) {
            playAnim(basicAttackContainer, EFBowAnimations.ELBOW_2);
        }
        if (comboCounter == 2) {
            playAnim(basicAttackContainer, EFBowAnimations.ELBOW_3);
        }
    }

    private void playAnim(SkillContainer basicAttackContainer, AnimationManager.AnimationAccessor<? extends MainFrameAnimation> accessor) {
        basicAttackContainer.getExecutor().playAnimationSynchronized(accessor, 0.15F);
        ComboAttacks.setComboCounterWithEvent(ModifyComboCounter.Causal.ANOTHER_ACTION_ANIMATION, basicAttackContainer.getServerExecutor(), basicAttackContainer, accessor, 0);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public List<Component> getTooltipOnItem(ItemStack itemStack, CapabilityItem cap, PlayerPatch<?> playerpatch) {
        List<Component> list = new ArrayList<>();
        list.add(Component.translatable("item.p1nero_bow.mortis_innate.tooltip0").withStyle(ChatFormatting.WHITE).append(Component.literal(String.format("[%.0f]", this.consumption / 2.0F)).withStyle(ChatFormatting.AQUA)));
        list.add(Component.translatable("item.p1nero_bow.mortis_innate.tooltip1", Minecraft.getInstance().options.keyDown.getTranslatedKeyMessage().copy().withStyle(ChatFormatting.GOLD)).withStyle(ChatFormatting.DARK_GRAY));
        list.add(Component.translatable("item.p1nero_bow.mortis_innate.tooltip2", EpicFightKeyMappings.WEAPON_INNATE_SKILL.getTranslatedKeyMessage().copy().withStyle(ChatFormatting.GOLD)).withStyle(ChatFormatting.DARK_GRAY));
        list.add(Component.translatable("item.p1nero_bow.mortis_innate.tooltip3").withStyle(ChatFormatting.DARK_GRAY));
        return list;
    }

    @Override
    public ResourceLocation getSkillTexture() {
        return ResourceLocation.fromNamespaceAndPath(EpicFightBowMod.MOD_ID, "textures/mob_effect/double_arrow.png");
    }

    @Override
    protected void generateTooltipforPhase(List<Component> list, ItemStack itemstack, CapabilityItem itemcap, PlayerPatch<?> playerpatch, Map<AnimationProperty.AttackPhaseProperty<?>, Object> propertyMap, String title) {
    }
}
