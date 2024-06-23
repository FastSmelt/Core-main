package gg.pots.basics.bukkit.command;

import gg.pots.basics.bukkit.CoreConstants;
import gg.pots.basics.bukkit.util.CC;
import gg.pots.basics.bukkit.util.command.CommandExecutorUtility;
import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;
import io.github.nosequel.command.exception.ConditionFailedException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CoreCommands {

    @Command(label = "discord", aliases = "dc")
    public void discord(BukkitCommandExecutor executor) {
        executor.sendMessage(CoreConstants.DISCORD_MESSAGE);
    }

    @Command(label = "store")
    public void store(BukkitCommandExecutor executor) {
        executor.sendMessage(CoreConstants.STORE_MESSAGE);
    }

    @Command(label = "website", aliases = "web")
    public void website(BukkitCommandExecutor executor) {
        executor.sendMessage(CoreConstants.WEBSITE_MESSAGE);
    }

    @Command(label = "yt")
    public void viewRequirements(BukkitCommandExecutor executor) {
        CommandExecutorUtility.sendMessage(executor, new String[] {
                CC.translate("&7&m----------------------------------------"),
                CC.translate("&d&lMedia Requirements"),
                CC.translate(""),
                CC.translate(" &f100-300 subscribers."),
                CC.translate(" &fMust upload at least once a week."),
                CC.translate(""),
                CC.translate("&d&lFamous Requirements"),
                CC.translate(""),
                CC.translate(" &f300-600 subscribers."),
                CC.translate(" &fMust upload at least once a week."),
                CC.translate(""),
                CC.translate("&d&lPartner Requirements"),
                CC.translate(" &f700+ subscribers."),
                CC.translate(" &fPlease create a ticket in the discord"),
                CC.translate(" &fif you wish to partner with us!"),
                CC.translate("&7&m----------------------------------------")
        });
    }

    @Command(label = "clearinventory", aliases = {"ci", "clearinv"}, permission = "core.staff", userOnly = true)
    public void clearInventory(BukkitCommandExecutor executor) {
        executor.getPlayer().getInventory().clear();
        executor.sendMessage(CC.translate("&aCleared your inventory!"));
    }

    @Command(label = "craft", permission = "core.craft")
    public void craft(BukkitCommandExecutor executor) {
        executor.getPlayer().openWorkbench(executor.getPlayer().getLocation(), true);
    }

    @Command(label = "feed", aliases = "eat", permission = "core.feed", userOnly = true)
    public void feed(BukkitCommandExecutor executor) {
        if (executor.getPlayer().getFoodLevel() == 20) {
            executor.sendMessage(CC.translate("&cYou are already at full hunger!"));
        } else {
            executor.getPlayer().setFoodLevel(20);
            executor.sendMessage(CC.translate("&aYou have been fed!"));
        }
    }

    @Command(label = "heal", permission = "core.heal", userOnly = true)
    public void heal(BukkitCommandExecutor executor) {
        if (executor.getPlayer().getHealth() == 20) {
            executor.sendMessage(CC.translate("&cYou are already at full health!"));
        } else {
            executor.getPlayer().setHealth(20);
            executor.sendMessage(CC.translate("&aYou have been healed!"));
        }
    }

    @Command(label = "rename", permission = "core.rename", userOnly = true)
    public void rename(BukkitCommandExecutor executor, String name) throws ConditionFailedException {
        if (executor.getPlayer().getItemInHand() == null) {
            throw new ConditionFailedException(CC.translate("&cYou must be holding an item to rename it!"));
        }

        final ItemStack itemStack = executor.getPlayer().getItemInHand();
        final ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(CC.translate(name));
        itemStack.setItemMeta(itemMeta);

        executor.getPlayer().updateInventory();
        executor.sendMessage(CC.translate("&fYou have renamed your item to " + name));
    }

    @Command(label = "more", permission = "core.more", userOnly = true)
    public void more(BukkitCommandExecutor executor) throws ConditionFailedException {
        if (executor.getPlayer().getItemInHand() == null) {
            throw new ConditionFailedException(CC.translate("&cYou must be holding an item to rename it!"));
        }

        executor.getPlayer().getItemInHand().setAmount(64);
        executor.sendMessage(CC.translate("&eYou have given yourself 64 of the item in your hand."));
    }

    @Command(label = "invsee", permission = "core.invsee", userOnly = true)
    public void viewInventory(BukkitCommandExecutor executor, Player target) {
        executor.getPlayer().openInventory(target.getInventory());

        executor.sendMessage(CC.translate("&eYou are now viewing " + target.getName() + "'s inventory."));
    }

    @Command(label = "top", permission = "core.top", userOnly = true)
    public void top(BukkitCommandExecutor executor) {
        executor.getPlayer().teleport(executor.getPlayer().getWorld().getHighestBlockAt(executor.getPlayer().getLocation()).getLocation());

        executor.sendMessage(CC.translate("&eYou have been teleported to the highest block in the world."));
    }

    @Command(label = "day", permission = "core.time", userOnly = true)
    public void day(BukkitCommandExecutor executor) {
        executor.getPlayer().getWorld().setTime(0);
        executor.sendMessage(CC.translate("&eYou have set the time to day."));
    }

    @Command(label = "night", permission = "core.time", userOnly = true)
    public void night(BukkitCommandExecutor executor) {
        executor.getPlayer().getWorld().setTime(14000);
        executor.sendMessage(CC.translate("&eYou have set the time to night."));
    }

    @Command(label = "kill", permission = "core.kill", userOnly = true)
    public void kill(BukkitCommandExecutor executor, Player target) {
        target.setHealth(0);
        executor.sendMessage(CC.translate("&eYou have killed " + target.getName() + "."));
    }
}