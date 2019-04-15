package us.blockcade.core;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import us.blockcade.core.commands.administrative.*;
import us.blockcade.core.commands.general.*;
import us.blockcade.core.commands.debugging.HeadsupCommand;
import us.blockcade.core.commands.moderation.BanCommand;
import us.blockcade.core.commands.moderation.KickCommand;
import us.blockcade.core.commands.moderation.MuteCommand;
import us.blockcade.core.commands.moderation.PunishCommand;
import us.blockcade.core.commands.moderation.gamemode.AdventureCommand;
import us.blockcade.core.commands.moderation.gamemode.CreativeCommand;
import us.blockcade.core.commands.moderation.gamemode.SpectatorCommand;
import us.blockcade.core.commands.moderation.gamemode.SurvivalCommand;
import us.blockcade.core.common.MiscHandler;
import us.blockcade.core.common.chat.ChatFormater;
import us.blockcade.core.common.environment.EnvironmentHandler;
import us.blockcade.core.common.format.HeaderHandler;
import us.blockcade.core.common.format.StatusMessages;
import us.blockcade.core.common.game.GameManager;
import us.blockcade.core.common.modules.chatstar.ChatStarManager;
import us.blockcade.core.common.punishment.PunishmentHandler;
import us.blockcade.core.common.punishment.PunishmentManager;
import us.blockcade.core.common.security.CommandBlocker;
import us.blockcade.core.common.security.StaffAuthenticator;
import us.blockcade.core.util.Testing;
import us.blockcade.core.util.api.event.server.ServerLoadEvent;
import us.blockcade.core.util.api.event.server.ServerUnloadEvent;
import us.blockcade.core.util.bungee.BungeeUtil;
import us.blockcade.core.util.gui.menu.GuiHandler;
import us.blockcade.core.util.sql.BlockcadeSql;
import us.blockcade.core.util.userdata.BlockcadeUsers;

public class Main extends JavaPlugin {

    private static Main main;
    public static Plugin getInstance() { return main; }

    @Override
    public void onEnable() {
        main = this;

        getConfig().options().copyDefaults(true);
        saveConfig();

        BlockcadeSql.openConnection();
        BlockcadeUsers.createUsersTable();
        StaffAuthenticator.createAuthTable();
        PunishmentManager.createPunishmentTable();
        ChatStarManager.createChatStarTable();

        BungeeUtil.getUtil().initialize();

        for (Player player : Bukkit.getOnlinePlayers())
            BlockcadeUsers.updateBPlayerCache(player.getUniqueId());

        getCommand("help").setExecutor(new HelpCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("survival").setExecutor(new SurvivalCommand());
        getCommand("creative").setExecutor(new CreativeCommand());

        getCommand("adventure").setExecutor(new AdventureCommand());
        getCommand("spectator").setExecutor(new SpectatorCommand());
        getCommand("ao").setExecutor(new AoCommand());
        getCommand("punish").setExecutor(new PunishCommand());
        getCommand("ban").setExecutor(new BanCommand());
        getCommand("mute").setExecutor(new MuteCommand());
        getCommand("kick").setExecutor(new KickCommand());
        getCommand("setrank").setExecutor(new SetrankCommand());
        getCommand("environment").setExecutor(new EnvironmentCommand());
        getCommand("setspawn").setExecutor(new SetspawnCommand());
        getCommand("headsup").setExecutor(new HeadsupCommand());
        getCommand("recache").setExecutor(new RecacheCommand());
        getCommand("givecoins").setExecutor(new GivecoinsCommand());
        getCommand("chatstar").setExecutor(new ChatstarCommand());
        getCommand("connect").setExecutor(new ConnectCommand());

        getServer().getPluginManager().registerEvents(new Testing(), this);
        getServer().getPluginManager().registerEvents(new GuiHandler(), this);
        getServer().getPluginManager().registerEvents(new MiscHandler(), this);
        getServer().getPluginManager().registerEvents(new GameManager(), this);
        getServer().getPluginManager().registerEvents(new ChatFormater(), this);
        getServer().getPluginManager().registerEvents(new HeaderHandler(), this);
        getServer().getPluginManager().registerEvents(new BlockcadeUsers(), this);
        getServer().getPluginManager().registerEvents(new StatusMessages(), this);
        getServer().getPluginManager().registerEvents(new CommandBlocker(), this);
        getServer().getPluginManager().registerEvents(new PunishmentManager(), this);
        getServer().getPluginManager().registerEvents(new PunishmentHandler(), this);
        getServer().getPluginManager().registerEvents(new EnvironmentHandler(), this);
        getServer().getPluginManager().registerEvents(new StaffAuthenticator(), this);

        ServerLoadEvent loadEvent = new ServerLoadEvent(Main.getInstance());
        getServer().getPluginManager().callEvent(loadEvent);
    }

    @Override
    public void onDisable() {
        BlockcadeSql.closeConnection();

        ServerUnloadEvent unloadEvent = new ServerUnloadEvent(Main.getInstance());
        getServer().getPluginManager().callEvent(unloadEvent);
    }

    public static FileConfiguration getConfiguration() {
        return getInstance().getConfig();
    }

    public static BungeeUtil getBungeeUtil() {
        return BungeeUtil.getUtil();
    }

}
