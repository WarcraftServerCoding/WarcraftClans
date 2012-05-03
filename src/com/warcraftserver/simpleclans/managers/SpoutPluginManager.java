package com.warcraftserver.simpleclans.managers;

import com.warcraftserver.simpleclans.Clan;
import com.warcraftserver.simpleclans.ClanPlayer;
import com.warcraftserver.simpleclans.Helper;
import com.warcraftserver.simpleclans.SimpleClans;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.player.SpoutPlayer;

import static org.getspout.spoutapi.SpoutManager.*;

/**
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
     *
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
     *
     * @param player the player
     */
    public void processPlayer(Player player)
    {
        if (isHasSpout())
        {
            ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);

            if (cp != null && cp.getClan().isVerified())
            {
                Clan clan = cp.getClan();

                SpoutPlayer sp = getPlayer(player);

                if (plugin.getSettingsManager().isClanCapes())
                {
                    if (!clan.getCapeUrl().isEmpty())
                    {
                        if (clan.getCapeUrl().toLowerCase().contains(".png"))
                        {
                            sp.setCape(clan.getCapeUrl());
                        }
                    }
                    else
                    {
                        if (plugin.getSettingsManager().getDefaultCapeUrl().toLowerCase().contains(".png"))
                        {
                            sp.setCape(plugin.getSettingsManager().getDefaultCapeUrl());
                        }
                    }
                }
//          DISABLED TO ALLOW FOR OUR SPECIAL CHAT FEATURE
//                if (plugin.getSettingsManager().isInGameTags())
//                {
//                    if (player.isSneaking())
//                    {
//                        sp.setTitle(player.getName());
//                    }
//                    else
//                    {
//                        String tag = plugin.getSettingsManager().isInGameTagsColored() ? (plugin.getSettingsManager().getTagBracketColor() + plugin.getSettingsManager().getTagBracketLeft() + clan.getColorTag() + plugin.getSettingsManager().getTagBracketColor() + plugin.getSettingsManager().getTagBracketRight() + plugin.getSettingsManager().getTagSeparatorColor() + plugin.getSettingsManager().getTagSeparator()) : ChatColor.DARK_GRAY + plugin.getSettingsManager().getTagBracketLeft() + clan.getTag() + plugin.getSettingsManager().getTagBracketRight() + plugin.getSettingsManager().getTagSeparator();
//                        sp.setTitle(tag + ChatColor.WHITE + player.getName());
//                    }
//                }
            }
        }
    }

    /**
     * Clear a player's cape
     *
     * @param player
     */
    public void clearCape(Player player)
    {
        if (isHasSpout())
        {
            ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);

            if (cp != null && cp.getClan().isVerified())
            {
                SpoutPlayer sp = getPlayer(player);
                sp.setCape("");
            }
        }
    }

    /**
     * Plays alert to player
     *
     * @param player
     */
    public void playAlert(Player player)
    {
        if (isHasSpout())
        {
            SpoutPlayer sp = getPlayerFromId(player.getEntityId());
            getSoundManager().playCustomSoundEffect(plugin, sp, plugin.getSettingsManager().getAlertUrl(), true);
        }
    }

    private boolean checkSpout()
    {
        Plugin test = plugin.getServer().getPluginManager().getPlugin("Spout");

        if (test != null)
        {
            SimpleClans.log(plugin.getLang("spout.features.enabled"));
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
