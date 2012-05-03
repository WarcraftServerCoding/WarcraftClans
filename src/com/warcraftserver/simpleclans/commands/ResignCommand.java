package com.warcraftserver.simpleclans.commands;

import com.warcraftserver.simpleclans.ChatBlock;
import com.warcraftserver.simpleclans.Clan;
import com.warcraftserver.simpleclans.ClanPlayer;
import com.warcraftserver.simpleclans.Helper;
import com.warcraftserver.simpleclans.SimpleClans;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 *
 * @author phaed
 */
public class ResignCommand
{
    public ResignCommand()
    {
    }

    /**
     * Execute the command
     * @param player
     * @param arg
     */
    public void execute(Player player, String[] arg)
    {
        SimpleClans plugin = SimpleClans.getInstance();

        if (plugin.getPermissionsManager().has(player, "simpleclans.member.resign"))
        {
            ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);

            if (cp != null)
            {
                Clan clan = cp.getClan();

                if (!clan.isLeader(player) || clan.getLeaders().size() > 1)
                {
                    clan.addBb(player.getName(), ChatColor.AQUA + MessageFormat.format(plugin.getLang("0.has.resigned"), Helper.capitalize(player.getName())));
                    clan.removePlayerFromClan(player.getName());
                }
                else if (clan.isLeader(player) && clan.getLeaders().size() == 1)
                {
                    plugin.getClanManager().serverAnnounce(ChatColor.AQUA + MessageFormat.format(plugin.getLang("clan.has.been.disbanded"), clan.getName()));
                    clan.disband();
                }
                else
                {
                    ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("last.leader.cannot.resign.you.must.appoint.another.leader.or.disband.the.clan"));
                }
            }
            else
            {
                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("not.a.member.of.any.clan"));
            }
        }
        else
        {
            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("insufficient.permissions"));
        }
    }
}
