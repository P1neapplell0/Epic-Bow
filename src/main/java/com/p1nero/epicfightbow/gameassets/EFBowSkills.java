package com.p1nero.epicfightbow.gameassets;

import com.p1nero.epicfightbow.EpicFightBowMod;
import com.p1nero.epicfightbow.skills.MortisBowInnateSkill;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import yesman.epicfight.registry.entries.EpicFightSkills;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;

public class EFBowSkills {
    public static final DeferredRegister<Skill> SKILLS = DeferredRegister.create(EpicFightSkills.REGISTRY.getRegistryKey(), EpicFightBowMod.MOD_ID);

    public static final DeferredHolder<Skill, MortisBowInnateSkill> MORTIS_INNATE = SKILLS.register("mortis_innate",
            id -> WeaponInnateSkill.createWeaponInnateBuilder(MortisBowInnateSkill::new)
                    .setResource(Skill.Resource.NONE)
                    .setActivateType(Skill.ActivateType.TOGGLE)
                    .build(id));
}
