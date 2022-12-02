package dev.panda.ability.api;

import dev.panda.ability.api.ability.IAbility;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class PandaAbilityAPI {

    /**
     * This method is used to register a conditional to all abilities
     *
     * @param name Id of the conditional
     * @param ability The ability that the conditional will have
     * @param conditional The conditional
     */
    public static void registerConditional(String name, IAbility ability, Predicate<Player> conditional) {
        throw new RuntimeException("API no is initialized!");
    }

    /**
     * This method is used to un-register conditionals to a specific ability
     *
     * @param name Id of the conditional
     * @param ability The ability that the conditional has
     */
    public static void unregisterConditional(String name, IAbility ability) {
        throw new RuntimeException("API no is initialized!");
    }

    /**
     * This method is used to register a conditional to a specific ability
     *
     * @param name Id of the conditional
     * @param conditional The conditional
     */
    public static void registerGlobalConditional(String name, Predicate<Player> conditional) {
        throw new RuntimeException("API no is initialized!");
    }

    /**
     * This method is used to un-register conditionals to all abilities
     *
     * @param name Id of the conditional
     */
    public static void unregisterGlobalConditional(String name) {
        throw new RuntimeException("API no is initialized!");
    }

    /**
     * This method allows quick access to abilities by name.
     *
     * @param name The name of the ability
     * @return The ability found or null
     */
    public static IAbility getAbility(String name) {
        throw new RuntimeException("API no is initialized!");
    }

    /**
     * This method allows quick access to abilities by Class.
     *
     * @param clazz The name of the ability
     * @return The ability found or null
     */
    public static IAbility getAbility(Class<? extends IAbility> clazz) {
        throw new RuntimeException("API no is initialized!");
    }

    /**
     * Method to register custom abilities
     *
     * @param ability The previously configured ability
     */
    public static void registerAbility(IAbility ability) {
        throw new RuntimeException("API no is initialized!");
    }

    /**
     * This method is used to obtain a list with the abilities that a player has cooldown
     *
     * @param player The player you want to get from
     * @return A list with the abilities
     */
    public static Set<IAbility> getActiveAbilities(Player player) {
        throw new RuntimeException("API no is initialized!");
    }

    /**
     * This method is used to see all the registered abilities, both internal and external.
     *
     * @return A list of all registered abilities
     */
    public static List<IAbility> getAbilities() {
        throw new RuntimeException("API no is initialized!");
    }

    /**
     * This method is used to verify if it has a global cooldown
     *
     * @param player The player you want to get from
     * @return If have global cooldown
     */
    public static boolean hasGlobalCooldown(Player player) {
        throw new RuntimeException("API no is initialized!");
    }

    /**
     * This method is used to obtain the global cooldown time for a specific player.
     *
     * @param player The player you want to get from
     * @return The global cooldown
     */
    public static String getGlobalCooldown(Player player) {
        throw new RuntimeException("API no is initialized!");
    }
}