package gg.pots.basics.bukkit.command;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.profile.ProfileService;
import gg.pots.basics.core.rank.Rank;
import gg.pots.basics.core.rank.RankService;
import io.github.nosequel.command.annotation.Command;
import io.github.nosequel.command.annotation.Subcommand;
import io.github.nosequel.command.bukkit.executor.BukkitCommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ListCommand {

    private final ProfileService profileModule = ServiceHandler.getInstance().find(ProfileService.class);
    private final RankService rankModule = ServiceHandler.getInstance().find(RankService.class);

    @Command(label = "list")
    public void list(BukkitCommandExecutor executor) {
        executor.sendMessage(this.getRankString(false));
        executor.sendMessage(ChatColor.WHITE + "(" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + ") " + this.getProfileString());

        if (executor.getPlayer().hasPermission("core.admin")) {
            executor.sendMessage(ChatColor.YELLOW + "There are " + this.rankModule.stream().filter(Rank::isHidden).count() + " hidden ranks, do /list all to view them.");
        }
    }

    @Subcommand(label = "all", parentLabel = "list", permission = "core.admin")
    public void listAll(BukkitCommandExecutor executor) {
        executor.sendMessage(this.getRankString(true));
        executor.sendMessage(ChatColor.WHITE + "(" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + ") " + this.getProfileString());
    }

    /**
     * Get the string sent for the rank string
     *
     * @param hidden whether it should include hidden ranks or not
     * @return the string o ranks
     */
    private String getRankString(boolean hidden) {
        return ChatColor.translateAlternateColorCodes('&', this.rankModule.stream()
                .filter(rank -> !rank.isHidden() || hidden)
                .map(rank -> (rank.isHidden() ? ChatColor.GRAY + "*" : "") + rank.getDisplayName())
                .collect(Collectors.joining(ChatColor.WHITE + ", ")));
    }

    /**
     * Get the string sent for the profile string
     *
     * @return the string o profiles
     */

    private String getProfileString() {
        return "[" + Bukkit.getOnlinePlayers().stream()
                .map(player -> this.profileModule.findOrElseMake(player.getUniqueId(), player.getName()))
                .sorted(Comparator.comparingInt(profile -> -profile.getPrimaryGrant().getRank().getWeight()))
                .map(profile -> ChatColor.translateAlternateColorCodes('&', ChatColor.valueOf(profile.getPrimaryGrant().getRank().getColor().name()) + profile.getName()))
                .collect(Collectors.joining(ChatColor.WHITE + ", ")) + ChatColor.WHITE + "]";
    }
}
