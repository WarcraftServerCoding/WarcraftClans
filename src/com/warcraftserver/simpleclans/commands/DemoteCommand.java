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
public class DemoteCommand
{
    public DemoteCommand()
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

        if (plugin.getPermissionsManager().has(player, "simpleclans.leader.demote"))
        {
            ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);

            if (cp != null)
            {
                Clan clan = cp.getClan();

                if (clan.isLeader(player))
                {
                    if (arg.length == 1)
                    {
                        String demotedName = arg[0];

                        if (clan.allOtherLeadersOnline(demotedName))
                        {
                            if (clan.isLeader(demotedName))
                            {
                                if (clan.getLeaders().size() == 1|| !plugin.getSettingsManager().isConfirmationForDemote())
                                {
                                    clan.addBb(player.getName(), ChatColor.AQUA + MessageFormat.format(plugin.getLang("demoted.back.to.member"), Helper.capitalize(demotedName)));
                                    clan.demote(demotedName);
                                }
                                else
                                {
                                    plugin.getRequestManager().addDemoteRequest(cp, demotedName, clan);
                                    ChatBlock.sendMessage(player, ChatColor.AQUA + plugin.getLang("demotion.vote.has.been.requested.from.all.leaders"));
                                }
                            }
                            else
                            {
                                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("player.is.not.a.leader.of.your.clan"));
                            }
                        }
                        else
                        {
                            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("leaders.must.be.online.to.vote.on.demotion"));
                        }
                    }
                    else
                    {
                        ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(plugin.getLang("usage.demote.leader"), plugin.getSettingsManager().getCommandClan()));
                    }
                }
                else
                {
                    ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("no.leader.permissions"));
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
