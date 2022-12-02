package dev.panda.ability.api.ability.event;

import dev.panda.ability.api.ability.IAbility;
import lombok.Getter;
import org.bukkit.entity.Player;

/**
 * @author UKry
 * Created: 24/11/2022
 * Project PandaAbility
 **/

public class AbilityCooldownExpire extends AbilityEvent {

    protected final IAbility ability;


    /**
     * @param player The player who used the ability
     * @param ability The ability that was used
     */
    public AbilityCooldownExpire(Player player, IAbility ability) {
        super(player);
        this.ability = ability;
    }

    /**
     * @return Ability of the cooldown
     */
    public IAbility getAbility() {
        return ability;
    }
}