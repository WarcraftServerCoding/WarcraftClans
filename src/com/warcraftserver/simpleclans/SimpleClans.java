package com.warcraftserver.simpleclans;

import com.warcraftserver.simpleclans.managers.StorageManager;
import com.warcraftserver.simpleclans.managers.CommandManager;
import com.warcraftserver.simpleclans.managers.SpoutPluginManager;
import com.warcraftserver.simpleclans.managers.PermissionsManager;
import com.warcraftserver.simpleclans.managers.ClanManager;
import com.warcraftserver.simpleclans.managers.SettingsManager;
import com.warcraftserver.simpleclans.managers.RequestManager;
import com.warcraftserver.simpleclans.managers.TeleportManager;
import com.warcraftserver.simpleclans.listeners.SCEntityListener;
import com.warcraftserver.simpleclans.listeners.SCPlayerListener;
import com.warcraftserver.simpleclans.commands.KOS;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;

import java.text.MessageFormat;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Phaed
 */
public class SimpleClans extends JavaPlugin
{
    private static SimpleClans instance;
    private static Logger logger = Logger.getLogger("Minecraft");
    private ClanManager clanManager;
    private RequestManager requestManager;
    private StorageManager storageManager;
    private SpoutPluginManager spoutPluginManager;
    private SettingsManager settingsManager;
    private PermissionsManager permissionsManager;
    private CommandManager commandManager;
    private TeleportManager teleportManager;
    private SCPlayerListener playerListener;
    private SCEntityListener entityListener;
    private Listener titlesListener;
    private static KOS cListener;


    private ResourceBundle lang;

    /**
     * @return the logger
     */
    public static Logger getLog()
    {
        return logger;
    }

    /**
     * @return the logger
     */
    public static void debug(String msg)
    {
        if (getInstance().getSettingsManager().isDebugging())
        {
            logger.log(Level.INFO, msg);
        }
    }

    /**
     * @return the instance
     */
    public static SimpleClans getInstance()
    {
        return instance;
    }

    public static void log(String msg, Object... arg)
    {
        if (arg == null || arg.length == 0)
        {
            logger.log(Level.INFO, msg);
        }
        else
        {
            logger.log(Level.INFO, new StringBuilder().append(MessageFormat.format(msg, arg)).toString());
        }
    }

    public void onEnable()
    {
        instance = this;
        settingsManager = new SettingsManager();

        lang = PropertyResourceBundle.getBundle("languages.lang");
        
        logger.info(MessageFormat.format(lang.getString("version.loaded"), getDescription().getName(), getDescription().getVersion()));

        spoutPluginManager = new SpoutPluginManager();
        permissionsManager = new PermissionsManager();
        requestManager = new RequestManager();
        clanManager = new ClanManager();
        storageManager = new StorageManager();
        commandManager = new CommandManager();
        teleportManager = new TeleportManager();

        playerListener = new SCPlayerListener();
        entityListener = new SCEntityListener();
        titlesListener = new TitlesListener(this);
        cListener = new KOS();

        getCommand("Kill-on-Sight").setExecutor(cListener);
        
        getServer().getPluginManager().registerEvents(entityListener, this);
        getServer().getPluginManager().registerEvents(playerListener, this);
        getServer().getPluginManager().registerEvents(titlesListener, this);

        spoutPluginManager.processAllPlayers();
    }

    public void onDisable()
    {
        ResourceBundle.clearCache();
        lang = null;
        getServer().getScheduler().cancelTasks(this);
        getStorageManager().closeConnection();
    }

    
    public Listener getTitlesListerner(){
        return titlesListener;
    }
    
    /**
     * @return the clanManager
     */
    public ClanManager getClanManager()
    {
        return clanManager;
    }

    /**
     * @return the requestManager
     */
    public RequestManager getRequestManager()
    {
        return requestManager;
    }

    /**
     * @return the storageManager
     */
    public StorageManager getStorageManager()
    {
        return storageManager;
    }

    /**
     * @return the spoutManager
     */
    public SpoutPluginManager getSpoutPluginManager()
    {
        return spoutPluginManager;
    }

    /**
     * @return the settingsManager
     */
    public SettingsManager getSettingsManager()
    {
        return settingsManager;
    }

    /**
     * @return the permissionsManager
     */
    public PermissionsManager getPermissionsManager()
    {
        return permissionsManager;
    }

    /**
     * @return the commandManager
     */
    public CommandManager getCommandManager()
    {
        return commandManager;
    }

    /**
     * @return the lang
     */
    public String getLang(String msg)
    {
        return lang.getString(msg);
    }

    public TeleportManager getTeleportManager()
    {
        return teleportManager;
    }
}
