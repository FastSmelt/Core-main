package gg.pots.basics.bukkit.command.rank;

import gg.pots.basics.bukkit.command.rank.menu.RanksMenu;
import gg.pots.basics.bukkit.util.CC;
import gg.pots.basics.bukkit.util.command.CommandExecutorUtility;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.rank.Rank;
import gg.pots.basics.core.rank.RankService;
import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.annotation.Help;
import io.github.nosequel.command.annotation.Subcommand;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;
import io.github.nosequel.command.exception.ConditionFailedException;
import org.bukkit.ChatColor;

import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Collectors;

public class RankCommands {

    private final RankService rankService = ServiceHandler.getInstance().find(RankService.class);

    @Command(label = "ranks", permission = "core.ranks", userOnly = true)
    public void ranks(BukkitCommandExecutor executor) {
        new RanksMenu(executor.getPlayer()).updateMenu();

        executor.sendMessage(CC.translate("&eYou have opened the ranks menu."));
    }

    @Help
    @Command(label = "rank", permission = "core.rank", userOnly = true)
    public void help(BukkitCommandExecutor executor) {}

    @Subcommand(
            label = "create",
            parentLabel = "rank",
            permission = "core.rank",
            userOnly = false,
            description = "Create a rank")
    public void createRank(BukkitCommandExecutor executor, String name) throws ConditionFailedException {
        if (this.rankService.find(name) != null) {
            throw new ConditionFailedException(CC.translate("&cA rank with that name already exists."));
        }

        final Rank rank = new Rank(UUID.randomUUID(), name);
        this.rankService.syncRank(rank);

        executor.sendMessage(CC.translate("&fSuccessfully created the rank with the name &a" + name + "&f."));
    }

    @Subcommand(
            label = "delete",
            parentLabel = "rank",
            permission = "core.rank",
            userOnly = false,
            description = "Delete a rank",
            weight = 1)
    public void deleteRank(BukkitCommandExecutor executor, Rank rank) {
        this.rankService.deleteRank(rank);

        executor.sendMessage(CC.translate("&fSuccessfully deleted the rank with the name &a" + rank.getDisplayName() + "&f."));
    }

    @Subcommand(
            label = "setdisplayname",
            parentLabel = "rank",
            permission = "core.rank",
            userOnly = false,
            description = "Set the display name of a rank",
            weight = 2)
    public void displayName(BukkitCommandExecutor executor, Rank rank, String displayName) {
        rank.setDisplayName(displayName);
        this.rankService.syncRank(rank);

        executor.sendMessage(CC.translate("&fSuccessfully set the display name of the rank &a" + rank.getDisplayName() + " &fto &a" + displayName + "&f."));
    }

    @Subcommand(
            label = "setprefix",
            parentLabel = "rank",
            permission = "core.rank",
            userOnly = false,
            description = "Set the prefix of a rank",
            weight = 3)
    public void prefix(BukkitCommandExecutor executor, Rank rank, String prefix) {
        rank.setPrefix(prefix);
        this.rankService.syncRank(rank);

        executor.sendMessage(CC.translate("&fSuccessfully set the prefix of the rank &a" + rank.getDisplayName() + " &fto &a" + prefix + "&f."));
    }

    @Subcommand(
            label = "setcolor",
            parentLabel = "rank",
            permission = "core.rank",
            userOnly = false,
            description = "Set the color of a rank",
            weight = 4)
    public void color(BukkitCommandExecutor executor, Rank rank, ChatColor color) {
        rank.setColor(color);
        this.rankService.syncRank(rank);

        executor.sendMessage(CC.translate("&fSuccessfully set the color of the rank &a" + rank.getDisplayName() + " &fto &a" + color.name() + "&f."));
    }

    @Subcommand(
            label = "setweight",
            parentLabel = "rank",
            permission = "core.rank",
            userOnly = false,
            description = "Set the weight of a rank",
            weight = 5)
    public void weight(BukkitCommandExecutor executor, Rank rank, Integer weight) {
        rank.setWeight(weight);
        this.rankService.syncRank(rank);

        executor.sendMessage(CC.translate("&fSuccessfully set the weight of the rank &a" + rank.getDisplayName() + " &fto &a" + weight + "&f."));
    }

    @Subcommand(
            label = "setdefault",
            parentLabel = "rank",
            permission = "core.rank",
            userOnly = false,
            description = "Set the default rank",
            weight = 6)
    public void setDefault(BukkitCommandExecutor executor, Rank rank, boolean def) {
        rank.setDefaultRank(def);
        this.rankService.syncRank(rank);

        executor.sendMessage(CC.translate("&fSuccessfully set the default rank to &a" + rank.getDisplayName() + "&f."));
    }

    @Subcommand(
            label = "permission",
            parentLabel = "rank",
            permission = "core.rank",
            userOnly = false,
            description = "Manage permissions of a rank",
            weight = 7)
    public void updatePermissions(BukkitCommandExecutor executor, Rank rank, String type, String permission) {
        switch (type) {
            case "add": {
                if (rank.getPermissions().contains(permission)) {
                    executor.sendMessage(CC.translate("&cThe rank already has that permission."));
                    return;
                }

                rank.addPermission(permission);

                this.rankService.saveRank(rank);
                this.rankService.syncRank(rank);

                executor.sendMessage(CC.translate("&fSuccessfully added the permission &a" + permission + " &fto the rank &a" + rank.getName() + "&f."));
            }
            break;

            case "remove": {
                if (!rank.getPermissions().contains(permission)) {
                    executor.sendMessage(CC.translate("&cThe rank does not have that permission."));
                    return;
                }

                rank.removePermission(permission);

                this.rankService.saveRank(rank);
                this.rankService.syncRank(rank);

                executor.sendMessage(CC.translate("&fSuccessfully removed the permission &a" + permission + " &fto the rank &a" + rank.getName() + "&f."));
            }
            break;

            default: {
                executor.sendMessage(CC.translate("&cInvalid type. Valid types: add, remove"));
            }
        }
    }

    @Subcommand(
            label = "inheritance",
            parentLabel = "rank",
            permission = "core.rank",
            userOnly = false,
            description = "Manage inheritance of a rank",
            weight = 8)
    public void updateInheritance(BukkitCommandExecutor executor, Rank rank, String type, Rank target) {
        switch (type) {
            case "add": {
                if (rank.getInherits().contains(target.getUuid())) {
                    executor.sendMessage(CC.translate("&cThe rank already inherits from that rank."));
                    return;
                }

                rank.getInherits().add(target.getUuid());

                this.rankService.saveRank(rank);
                this.rankService.syncRank(rank);

                executor.sendMessage(CC.translate("&fSuccessfully added the inheritance &a" + target.getName() + " &fto the rank &a" + rank.getDisplayName() + "&f."));
            }
            break;

            case "remove": {
                if (!rank.getInherits().contains(target.getUuid())) {
                    executor.sendMessage(CC.translate("&cThe rank does not inherit from that rank."));
                    return;
                }

                rank.getInherits().remove(target.getUuid());

                this.rankService.saveRank(rank);
                this.rankService.syncRank(rank);

                executor.sendMessage(CC.translate("&fSuccessfully removed the inheritance &a" + target.getName() + " &fto the rank &a" + rank.getDisplayName() + "&f."));
            }
            break;

            default: {
                executor.sendMessage(CC.translate("&cInvalid type. Valid types: add, remove"));
            }
        }
    }

    @Subcommand(
            label = "info",
            parentLabel = "rank",
            permission = "core.rank",
            userOnly = false,
            description = "Get information about a rank",
            weight = 9)
    public void information(BukkitCommandExecutor executor, Rank rank) {
        CommandExecutorUtility.sendMessage(executor, new String[] {
                CC.translate("&7&m----------------------------------------"),
                CC.translate("&eRank Information for " + rank.getDisplayName()),
                "",
                CC.translate("&eUUID: &f" + rank.getUuid().toString()),
                CC.translate("&ePrefix: " + rank.getPrefix()),
                CC.translate("&eWeight: &a" + rank.getWeight()),
                CC.translate("&eColor: " + rank.getColor() + rank.getColor().name()),
                CC.translate("&eDefault: &a" + rank.isDefaultRank()),
                "",
                CC.translate("&ePermissions: &a" + String.join(", ", rank.getPermissions())),
                CC.translate("&eInheritances: &a" + rank.getInherits().stream()
                        .map(inherit -> this.rankService.find(inherit).getDisplayName())
                        .collect(Collectors.joining(", "))),
                CC.translate("&7&m----------------------------------------")
        });
    }

    @Subcommand(
            label = "list",
            parentLabel = "rank",
            permission = "core.rank",
            userOnly = false,
            description = "List all ranks",
            weight = 10)
    public void list(BukkitCommandExecutor executor) {
        this.rankService.stream()
                .sorted(Comparator.comparingInt(Rank::getWeight).reversed())
                .map(rank -> CC.translate(rank.getDisplayName() + ChatColor.GRAY + " - " + ChatColor.GOLD + "Weight: " + ChatColor.YELLOW + rank.getWeight()))
                .collect(Collectors.toList()).forEach(executor::sendMessage);
    }

    @Command(label = "clearperms", permission = "core.rank")
    public void clearPermissions(BukkitCommandExecutor executor, Rank rank) {
        rank.getPermissions().clear();
        this.rankService.syncRank(rank);

        executor.sendMessage(CC.translate("&fSuccessfully cleared the permissions of the rank &a" + rank.getDisplayName() + "&f."));
    }
}