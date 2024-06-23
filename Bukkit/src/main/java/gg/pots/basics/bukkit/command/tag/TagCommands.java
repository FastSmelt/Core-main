package gg.pots.basics.bukkit.command.tag;

import gg.pots.basics.bukkit.command.tag.menu.TagsMenu;
import gg.pots.basics.bukkit.util.CC;
import gg.pots.basics.bukkit.util.command.CommandExecutorUtility;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.profile.ProfileService;
import gg.pots.basics.core.tag.Tag;
import gg.pots.basics.core.tag.TagService;
import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.annotation.Help;
import io.github.nosequel.command.annotation.Subcommand;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;
import io.github.nosequel.command.exception.ConditionFailedException;

import java.util.UUID;

public class TagCommands {

    private final ProfileService profileService = ServiceHandler.getInstance().find(ProfileService.class);
    private final TagService tagService = ServiceHandler.getInstance().find(TagService.class);

    @Command(label = "tags", userOnly = true)
    public void tags(BukkitCommandExecutor executor) {
        new TagsMenu(executor.getPlayer()).updateMenu();
    }

    @Help
    @Command(label = "tag", permission = "core.tag", userOnly = true)
    public void help(BukkitCommandExecutor executor) {}

    @Subcommand(
            label = "create",
            parentLabel = "tag",
            permission = "core.tag",
            userOnly = true,
            description = "Createa a tag"
    )
    public void createTag(BukkitCommandExecutor executor, String name) throws ConditionFailedException {
        if (this.tagService.find(name) != null) {
            throw new ConditionFailedException(CC.translate("&cA tag with that name already exists."));
        }

        final Tag tag = new Tag(UUID.randomUUID(), name);
        this.tagService.syncTag(tag);

        executor.sendMessage(CC.translate("&fSuccessfully created a tag with the name &a" + name + "&f."));
    }

    @Subcommand(
            label = "delete",
            parentLabel = "tag",
            permission = "core.tag",
            userOnly = true,
            description = "Delete a tag",
            weight = 1
    )
    public void deleteTag(BukkitCommandExecutor executor, Tag tag) {
        this.tagService.deleteTag(tag);

        executor.sendMessage(CC.translate("&fSuccessfully deleted the tag with the name &a" + tag.getDisplayName() + "&f."));
    }

    @Subcommand(
            label = "setdisplayname",
            parentLabel = "tag",
            permission = "core.tag",
            userOnly = true,
            description = "Set the display name of a tag",
            weight = 2
    )
    public void displayName(BukkitCommandExecutor executor, Tag tag, String displayName) {
        tag.setDisplayName(displayName);
        this.tagService.syncTag(tag);

        executor.sendMessage(CC.translate("&fSuccessfully set the display name of the tag &a" + tag.getName() + "&f to &a" + displayName + "&f."));
    }

    @Subcommand(
            label = "setprefix",
            parentLabel = "tag",
            permission = "core.tag",
            userOnly = true,
            description = "Set the prefix of a tag",
            weight = 3
    )
    public void prefix(BukkitCommandExecutor executor, Tag tag, String prefix) {
        tag.setPrefix(prefix);
        this.tagService.syncTag(tag);

        executor.sendMessage(CC.translate("&fSuccessfully set the prefix of the tag &a" + tag.getDisplayName() + "&f to &a" + prefix + "&f."));
    }

    @Subcommand(
            label = "seticon",
            parentLabel = "tag",
            permission = "core.tag",
            userOnly = true,
            description = "Set the icon of a tag",
            weight = 4
    )
    public void setIcon(BukkitCommandExecutor executor, Tag tag) {
        if (executor.getPlayer().getItemInHand() == null) {
            executor.sendMessage(CC.translate("&cYou must be holding an item in your hand."));
            return;
        }

        tag.setIcon(executor.getPlayer().getItemInHand());
        this.tagService.syncTag(tag);
        executor.sendMessage(CC.translate("&fSuccessfully set the icon of the tag &a" + tag.getDisplayName() + "&f."));
    }

    @Subcommand(
            label = "setpermission",
            parentLabel = "tag",
            permission = "core.tag",
            userOnly = true,
            description = "Set the permission of a tag",
            weight = 5
    )
    public void setPermission(BukkitCommandExecutor executor, Tag tag, String permission) {
        tag.setPermission(permission);
        this.tagService.syncTag(tag);

        executor.sendMessage(CC.translate("&fSuccessfully set the permission of the tag &a" + tag.getDisplayName() + "&f to &a" + permission + "&f."));
    }

    @Subcommand(
            label = "info",
            parentLabel = "tag",
            permission = "core.tag",
            userOnly = true,
            description = "Get information about a tag",
            weight = 6
    )
    public void information(BukkitCommandExecutor executor, Tag tag) {
        CommandExecutorUtility.sendMessage(executor, new String[] {
                CC.translate("&7&m----------------------------------------"),
                CC.translate("&eTag Information for " + tag.getDisplayName()),
                "",
                CC.translate("&fName: &a" + tag.getName()),
                CC.translate("&fPrefix: &a" + tag.getPrefix()),
                CC.translate("&fPermission: &a" + tag.getPermission()),
                CC.translate("&7&m----------------------------------------")
        });
    }

    @Subcommand(
            label = "list",
            parentLabel = "tag",
            permission = "core.tag",
            userOnly = true,
            description = "List all tags",
            weight = 7
    )
    public void list(BukkitCommandExecutor executor) {
        this.tagService.stream()
                .map(tag -> CC.translate("&f- &a" + tag.getName() + " &7(&a" + tag.getDisplayName() + "&7)"))
                .forEach(executor::sendMessage);
    }

    @Command(label = "cleartag", userOnly = true)
    public void clearTag(BukkitCommandExecutor executor) {
        final CoreProfile coreProfile = this.profileService.find(executor.getPlayer().getName());

        if (coreProfile.getActiveTag() == null) {
            executor.sendMessage(CC.translate("&cYou do not have an active tag."));
        } else {
            coreProfile.setActiveTag(null);
            executor.sendMessage(CC.translate("&eSuccessfully cleared your active tag."));
        }
    }
}
