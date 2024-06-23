package gg.pots.basics.bukkit;

import gg.pots.basics.bukkit.chat.ChatProcedureModule;
import gg.pots.basics.bukkit.chat.impl.*;
import gg.pots.basics.bukkit.chat.impl.grant.GrantSetReasonProcedure;
import gg.pots.basics.bukkit.command.CoreCommands;
import gg.pots.basics.bukkit.command.ListCommand;
import gg.pots.basics.bukkit.command.adapter.*;
import gg.pots.basics.bukkit.command.alts.AltsCommand;
import gg.pots.basics.bukkit.command.chat.ChatClearCommands;
import gg.pots.basics.bukkit.command.chat.ChatMuteCommand;
import gg.pots.basics.bukkit.command.chat.ChatSlowCommand;
import gg.pots.basics.bukkit.command.color.ColorCommand;
import gg.pots.basics.bukkit.command.fly.FlyCommand;
import gg.pots.basics.bukkit.command.gamemode.GameModeCommand;
import gg.pots.basics.bukkit.command.gem.GemCommands;
import gg.pots.basics.bukkit.command.grant.GrantCommand;
import gg.pots.basics.bukkit.command.grant.GrantsCommand;
import gg.pots.basics.bukkit.command.history.HistoryCommand;
import gg.pots.basics.bukkit.command.ignore.IgnoreCommand;
import gg.pots.basics.bukkit.command.lookup.LookupCommand;
import gg.pots.basics.bukkit.command.message.MessageCommands;
import gg.pots.basics.bukkit.command.permission.PermissionCommands;
import gg.pots.basics.bukkit.command.punishment.BanCommand;
import gg.pots.basics.bukkit.command.punishment.BlacklistCommand;
import gg.pots.basics.bukkit.command.punishment.KickCommand;
import gg.pots.basics.bukkit.command.punishment.MuteCommand;
import gg.pots.basics.bukkit.command.rank.RankCommands;
import gg.pots.basics.bukkit.command.report.report.ReportCommand;
import gg.pots.basics.bukkit.command.report.report.ReportSyncHandler;
import gg.pots.basics.bukkit.command.report.request.RequestCommand;
import gg.pots.basics.bukkit.command.report.request.RequestSyncHandler;
import gg.pots.basics.bukkit.command.setting.SettingsCommand;
import gg.pots.basics.bukkit.command.staff.StaffChatCommand;
import gg.pots.basics.bukkit.command.tag.TagCommands;
import gg.pots.basics.bukkit.command.teleport.TeleportCommands;
import gg.pots.basics.bukkit.command.toggle.TogglePMCommand;
import gg.pots.basics.bukkit.protocol.ProtocolHandler;
import gg.pots.basics.bukkit.protocol.impl.ProtocolPacketHandler;
import gg.pots.basics.bukkit.util.configuration.ConfigFile;
import gg.pots.basics.bukkit.listener.PlayerListener;
import gg.pots.basics.bukkit.message.StringSyncHandler;
import gg.pots.basics.bukkit.message.activity.StaffJoinSyncHandler;
import gg.pots.basics.bukkit.message.activity.StaffQuitSyncHandler;
import gg.pots.basics.bukkit.nametag.NameTagAdapter;
import gg.pots.basics.bukkit.task.BroadcastTask;
import gg.pots.basics.bukkit.util.CC;
import gg.pots.basics.bukkit.util.logger.LoggerUtility;
import gg.pots.basics.core.CoreAPI;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.profile.ProfileService;
import gg.pots.basics.core.profile.sync.ProfileDeleteHandler;
import gg.pots.basics.core.profile.sync.ProfileSyncHandler;
import gg.pots.basics.core.rank.Rank;
import gg.pots.basics.core.rank.RankService;
import gg.pots.basics.core.rank.sync.RankDeleteHandler;
import gg.pots.basics.core.rank.sync.RankSyncHandler;
import gg.pots.basics.core.saving.SavingService;
import gg.pots.basics.core.saving.sync.type.RedisSyncType;
import gg.pots.basics.core.saving.types.MongoSavingType;
import gg.pots.basics.core.tag.Tag;
import gg.pots.basics.core.tag.TagService;
import gg.pots.basics.core.tag.sync.TagDeleteHandler;
import gg.pots.basics.core.tag.sync.TagSyncHandler;
import io.github.nosequel.command.bukkit.BukkitCommandHandler;
import io.github.nosequel.menu.MenuHandler;
import io.github.thatkawaiisam.ostentus.Ostentus;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class CoreSpigotPlugin extends JavaPlugin {

    public static CoreSpigotPlugin instance;
    public final ServiceHandler handler = new ServiceHandler();

    public CoreAPI coreAPI;

    private ProtocolHandler protocolHandler;

    public ConfigFile messagesFile;
    public ConfigFile broadcastsFile;

    @Override
    public void onEnable() {
        instance = this;
        long startUp = System.currentTimeMillis();

        this.saveDefaultConfig();
        this.messagesFile = new ConfigFile(this, "messages.yml");
        this.broadcastsFile = new ConfigFile(this, "broadcasts.yml");

        this.coreAPI = new CoreAPI(ServiceHandler.getInstance());

        this.protocolHandler = new ProtocolPacketHandler();
        this.protocolHandler.initializePacket();

        final SavingService savingService = new SavingService(
                new MongoSavingType(CoreConstants.MONGO_HOSTNAME, CoreConstants.MONGO_PORT, CoreConstants.MONGO_DATABASE),
                new RedisSyncType(CoreConstants.REDIS_HOSTNAME, CoreConstants.REDIS_PORT)
        ).register(handler);

        final RankService rankService = new RankService(savingService).register(this.handler);
        final ProfileService profileService = new ProfileService(savingService).register(this.handler);
        final TagService tagService = new TagService(savingService).register(this.handler);

        savingService.getSyncType().registerHandler(new ProfileSyncHandler(profileService));
        savingService.getSyncType().registerHandler(new ProfileDeleteHandler(profileService));
        savingService.getSyncType().registerHandler(new RankSyncHandler(rankService));
        savingService.getSyncType().registerHandler(new RankDeleteHandler(rankService));
        savingService.getSyncType().registerHandler(new TagSyncHandler(tagService));
        savingService.getSyncType().registerHandler(new TagDeleteHandler(tagService));
        savingService.getSyncType().registerHandler(new StringSyncHandler());
        savingService.getSyncType().registerHandler(new StaffJoinSyncHandler());
        savingService.getSyncType().registerHandler(new StaffQuitSyncHandler());
        savingService.getSyncType().registerHandler(new ReportSyncHandler());
        savingService.getSyncType().registerHandler(new RequestSyncHandler());

        new ChatProcedureModule(this,
                new NormalChatProcedure(),
                new MutePunishmentChatProcedure(),
                new SlowChatChatProcedure(),
                new MuteChatChatProcedure(),
                new GrantSetReasonProcedure()).register(this.handler);

        final BukkitCommandHandler commandHandler = new BukkitCommandHandler("core");

        commandHandler.registerTypeAdapter(ChatColor.class, new ChatColorTypeAdapter());
        commandHandler.registerTypeAdapter(GameMode.class, new GameModeTypeAdapter());
        commandHandler.registerTypeAdapter(CoreProfile.class, new ProfileTypeAdapter());
        commandHandler.registerTypeAdapter(Rank.class, new RankTypeAdapter());
        commandHandler.registerTypeAdapter(Tag.class, new TagTypeAdapter());

        commandHandler.registerCommand(new CoreCommands());
        commandHandler.registerCommand(new ListCommand());

        commandHandler.registerCommand(new ColorCommand());

        commandHandler.registerCommand(new RequestCommand());
        commandHandler.registerCommand(new ReportCommand());

        commandHandler.registerCommand(new GameModeCommand());
        commandHandler.registerCommand(new FlyCommand());

        commandHandler.registerCommand(new MessageCommands());
        commandHandler.registerCommand(new TogglePMCommand());
        commandHandler.registerCommand(new IgnoreCommand());

        commandHandler.registerCommand(new SettingsCommand());
        commandHandler.registerCommand(new GemCommands());

        commandHandler.registerCommand(new TagCommands());

        commandHandler.registerCommand(new RankCommands());
        commandHandler.registerCommand(new PermissionCommands());

        commandHandler.registerCommand(new LookupCommand());

        commandHandler.registerCommand(new KickCommand());
        commandHandler.registerCommand(new MuteCommand());
        commandHandler.registerCommand(new BanCommand());
        commandHandler.registerCommand(new BlacklistCommand());

        commandHandler.registerCommand(new HistoryCommand());
        commandHandler.registerCommand(new AltsCommand());

        commandHandler.registerCommand(new GrantCommand());
        commandHandler.registerCommand(new GrantsCommand());

        commandHandler.registerCommand(new StaffChatCommand());
        commandHandler.registerCommand(new TeleportCommands());

        commandHandler.registerCommand(new ChatClearCommands());
        commandHandler.registerCommand(new ChatMuteCommand());
        commandHandler.registerCommand(new ChatSlowCommand());

        this.handler.loadAll();
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        new BroadcastTask().runTaskTimerAsynchronously(this, 0L, 20L * 60L);

        new Ostentus(this, new NameTagAdapter());
        new MenuHandler(this);

        LoggerUtility.sendMessage(CC.translate("&7[&eCore&7] &fSuccessfully enabled in &a" + (System.currentTimeMillis() - startUp) + "ms&f."));
    }

    @Override
    public void onDisable() {
        this.handler.unloadAll();

        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(CC.translate("&cThe server is restarting.")));

        LoggerUtility.sendMessage(CC.translate("&7[&eCore&7] &fSuccessfully disabled."));
    }
}