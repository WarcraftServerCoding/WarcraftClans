package net.sacredlabyrinth.phaed.simpleclans.managers;

import java.util.logging.Level;
import net.sacredlabyrinth.phaed.simpleclans.Helper;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

/**
 *
 * @author phaed
 */
public final class SpoutPluginManager
{
    private SimpleClans plugin;
    private boolean hasSpout;

    /**
     *
     */
    public SpoutPluginManager()
    {
        plugin = SimpleClans.getInstance();
        hasSpout = checkSpout();
    }

    /**
     * Process all players
     */
    public void processAllPlayers()
    {
        if (isHasSpout())
        {
            Player[] onlinePlayers = plugin.getServer().getOnlinePlayers();

            for (Player player : onlinePlayers)
            {
                processPlayer(player);
            }
        }
    }

    /**
     * Adds cape and title to a player
     * @param playerName
     */
    public void processPlayer(String playerName)
    {
        if (isHasSpout())
        {
            Player player = Helper.matchOnePlayer(playerName);

            if (player != null)
            {
                processPlayer(player);
            }
        }
    }

    /**
     * Adds cape and title to a player
     * @param playerName
     */
    public void processPlayer(Player player)
    {
        if (isHasSpout())
        {
            ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);

            if (cp != null && cp.getClan().isVerified())
            {
                Clan clan = cp.getClan();

                if (plugin.getSettingsManager().isClanCapes())
                {
                    if (!clan.getCapeUrl().isEmpty())
                    {
                        SpoutManager.getAppearanceManager().setGlobalCloak(player, clan.getCapeUrl());
                    }
                    else
                    {
                        SpoutManager.getAppearanceManager().setGlobalCloak(player, plugin.getSettingsManager().getDefaultCapeUrl());
                    }
                }

                if (plugin.getSettingsManager().isInGameTags())
                {
                    String tag = plugin.getSettingsManager().isInGameTagsColored() ? clan.getColorTag() + plugin.getSettingsManager().getTagSeparatorColor() + plugin.getSettingsManager().getTagSeparator() : ChatColor.DARK_GRAY + clan.getTag() + plugin.getSettingsManager().getTagSeparator();
                    SpoutManager.getAppearanceManager().setGlobalTitle(player, tag + ChatColor.WHITE + player.getName());
                }
            }
        }
    }

    /**
     * Plays alert to player
     * @param player
     */
    public void playAlert(Player player)
    {
        if (isHasSpout())
        {
            SpoutPlayer sp = org.getspout.spoutapi.SpoutManager.getPlayerFromId(player.getEntityId());
            SpoutManager.getSoundManager().playCustomSoundEffect(plugin, sp, plugin.getSettingsManager().getAlertUrl(), true);
        }
    }

    private boolean checkSpout()
    {
        Plugin test = plugin.getServer().getPluginManager().getPlugin("Spout");

        if (test != null)
        {
            SimpleClans.log(Level.INFO, "Spout features enabled");
            return true;
        }
        return false;
    }

    /**
     * @return the hasSpout
     */
    public boolean isHasSpout()
    {
        return hasSpout;
    }
}