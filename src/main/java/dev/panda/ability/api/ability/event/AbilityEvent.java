package dev.panda.ability.api.ability.event;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author UKry
 * Created: 24/11/2022
 * Project PandaAbility
 **/

public abstract class AbilityEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    protected final Player player;

    /**
     * @param player The player who used the ability
     */
    public AbilityEvent(Player player) {
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * This method is a required because the constructor no is empty
     * @return HandlerList to bukkit api
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * @return The player who used the ability
     */
    public Player getPlayer() {
        return player;
    }


    /**
     * Method to facilitate the call by the bukkit api
     * @see org.bukkit.plugin.PluginManager#callEvent(Event)
     */
    public void call() {
        Bukkit.getPluginManager().callEvent(this);
    }
}