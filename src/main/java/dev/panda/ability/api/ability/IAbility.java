package dev.panda.ability.api.ability;

import dev.panda.ability.api.ability.event.AbilityCooldownExpire;
import dev.panda.ability.api.ability.event.AbilityUseEvent;
import dev.panda.ability.api.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <b>The new abilities that are created based on this class
 * will not have compatibility with the preconditions of the api</b>
 *
 * @author UKry
 * Created: 29/11/2022
 * Project PandaAbility
 **/

public abstract class IAbility implements Listener {

    protected String name;
    protected String displayName = "New Ability";
    protected List<String> description = new ArrayList<>();
    /**
     * It is recommended not to modify this unless you have prior knowledge
     */
    protected String usesFormat = "Uses: %USES%";
    protected Material material = Material.AIR;
    protected boolean enable = true;
    protected boolean glow = false;
    protected boolean usages = true;
    protected int data = 0;
    protected int uses = 3;
    protected int cooldown = 60;
    protected boolean events = true;

    /**
     * Main constructor
     * @param name The identifier of the ability
     */
    public IAbility(String name) {
        this.name = name;
    }

    /**
     * Open method to declare the custom use of each ability
     * @param event The parameter provided after the corresponding verifications
     * @see IAbility#onRawInteract(PlayerInteractEvent) to more info of the verifications
     */
    public void onInteract(PlayerInteractEvent event) {}

    /**
     * This method is in charge of server-side functions.
     * @param event The parameter provided by Bukkit API
     * @see <a href="https://www.spigotmc.org/wiki/using-the-event-api/">Spigot events wki</a>
     */
    @EventHandler
    public void onRawInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack stack = player.getItemInHand();
        if(stack == null || stack.getType() == Material.AIR) return;
        if(!isEnable()) return;
        if(!isAbility(stack)) return;
        onInteract(event);
        if(!events) return;
        new AbilityUseEvent(player, this).call();
        Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("PandaAbility"), () -> new AbilityCooldownExpire(player, this).call(), this.cooldown * 20L);
    }

    /**
     * This method is used to obtain the item of the ability
     * @return The ability item
     */
    public ItemStack getItem() {
        return new ItemBuilder(this.material, 1, this.data)
                .name(this.displayName)
                .lore(this.description.stream().map(s -> s.replace("%USES%", String.valueOf(this.uses))).collect(Collectors.toList()))
                .enchant(this.glow, Enchantment.DURABILITY, 5)
                .build();
    }

    /**
     * This method is used to validate that the specified item is an ability
     * @param stack The item you want to check
     * @return If the item is an ability
     */
    public boolean isAbility(ItemStack stack) {
        if(stack == null) return false;
        if(stack.getType() == Material.AIR) return false;
        ItemStack ab = getItem();
        ItemMeta aMeta = ab.getItemMeta();
        ItemMeta meta = stack.getItemMeta();
        if(aMeta == null) return false;
        if(!meta.hasDisplayName()) return false;
        if(!meta.getDisplayName().equals(aMeta.getDisplayName())) return false;
        if(!meta.hasLore()) return false;
        List<String> aLore = aMeta.getLore().stream().map(ChatColor::stripColor).filter(s -> !s.contains(usesFormat.split(":")[0])).collect(Collectors.toList());
        List<String> lore = meta.getLore().stream().map(ChatColor::stripColor).filter(s -> !s.contains(usesFormat.split(":")[0])).collect(Collectors.toList());
        return aLore.equals(lore);
    }

    /**
     * If the item works with uses, check the remaining uses of the item
     * @param player The player you want to verify
     */
    public void checkUses(Player player) {
        int currentUses = getUses(player);
        currentUses--;
        if (currentUses <= 0) {
            player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 1F, 1F);
            player.setItemInHand(new ItemStack(Material.AIR));
            player.updateInventory();
        } else setUses(player, currentUses);
    }

    /**
     * Gets the uses of the item the player owns
     * @param player The player you want to verify
     * @return Remaining uses
     */
    public int getUses(Player player) {
        ItemMeta meta = player.getItemInHand().getItemMeta();
        if (meta == null) return 0;
        String text = meta.getLore().stream().filter(lore -> lore.contains(usesFormat.split(":")[0])).findFirst().orElse("NONE");
        if(text.equals("NONE")) return 0;
        String[] split = text.split(" ");
        return Integer.parseInt(ChatColor.stripColor(split[1]));
    }

    /**
     * Sets a new amount of uses on the specified player's item
     * @param player The player you want to modify
     * @param uses New uses amount
     */
    public void setUses(Player player, int uses) {
        ItemStack stack = player.getItemInHand();
        ItemMeta meta = stack.getItemMeta();
        meta.setLore(this.description.stream().map(s -> s.replace(usesFormat.split(":")[1].replace(" ", ""), String.valueOf(uses))).collect(Collectors.toList()));
        stack.setItemMeta(meta);
    }

    /**
     * Take the item from the player's inventory
     * @param player The player you want to take from
     */
    public void takeItem(Player player) {
        ItemStack inHand = player.getItemInHand();
        if(inHand.getAmount() == 1) {
            player.setItemInHand(new ItemStack(Material.AIR));
        } else {
            inHand.setAmount(inHand.getAmount() - 1);
        }
        player.setItemInHand(inHand);
        player.updateInventory();
    }

    /**
     * This abstract method is used to declare your own cooldown system.
     * @param player The player to which you want to add the cooldown
     */
    public abstract void applyCooldown(Player player);

    /**
     * This abstract method is used to check if the specified player has a cooldown.
     * @param player The player to which you want to add the cooldown
     * @return If player have a cooldown
     */
    public abstract boolean hasCooldown(Player player);

    /**
     * This abstract method is used to get the specified player cooldown
     * @param player The player to which you want to get the cooldown
     * @return The player cooldown in text format
     */
    public abstract String getCooldown(Player player);

    /**
     * @return Ability name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name New ability name
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * @return Ability display name
     */
    public String getDisplayName() {
        return displayName;
    }


    /**
     * @param displayName New ability display name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return Ability description
     */
    public List<String> getDescription() {
        return description;
    }

    /**
     * @param description New ability description
     */
    public void setDescription(List<String> description) {
        this.description = description;
    }

    /**
     * @return Ability material
     * @see org.bukkit.Material#values()
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * @param material New ability material
     */
    public void setMaterial(Material material) {
        this.material = material;
    }

    /**
     * @return If ability is enabled
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * @param enable Enable/Disable ability
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * @return If ability item have glow effect
     */
    public boolean isGlow() {
        return glow;
    }

    /**
     * @param glow Enable/Disable item glow effect
     */
    public void setGlow(boolean glow) {
        this.glow = glow;
    }

    /**
     * @return If ability have usages
     */
    public boolean isUsages() {
        return usages;
    }

    /**
     * @param usages Enable/Disable ability usages
     */
    public void setUsages(boolean usages) {
        this.usages = usages;
    }

    /**
     * @return Ability item data
     */
    public int getData() {
        return data;
    }

    /**
     * @param data New ability item data
     */
    public void setData(int data) {
        this.data = data;
    }

    /**
     * @return Ability uses
     */
    public int getUses() {
        return uses;
    }

    /**
     * @param uses New ability uses
     */
    public void setUses(int uses) {
        this.uses = uses;
    }

    /**
     * @return Ability cooldown time
     */
    public int getCooldown() {
        return cooldown;
    }

    /**
     * @param cooldown New time for ability cooldown
     */
    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }
}