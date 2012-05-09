package com.warcraftserver.simpleclans.commands;

import java.util.List;
import java.util.HashMap;
import java.io.*;
import java.util.logging.Logger;
import com.warcraftserver.simpleclans.Clan;
import com.warcraftserver.simpleclans.ClanPlayer;
import com.warcraftserver.simpleclans.SimpleClans;
import com.warcraftserver.simpleclans.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KOS implements ExecutorInterface
{
    SimpleClans plugin = SimpleClans.getInstance();
    Logger log = Logger.getLogger("Minecraft");
    ClanManager manager = SimpleClans.getInstance().getClanManager();
    HashMap<Player, String> index = new HashMap<Player, String>();
    File clans = new File("plugins/SimpleClans/clankos.txt");
    KOSManager kosmanager = new KOSManager();


    @Override
    public boolean onCommand(CommandSender sender, Command paramCommand, String paramString, String[] arg) {
        if(arg.length > 0 ){
            Player player = null;
            if(sender instanceof Player){
                player = (Player)sender;
            }
            ClanPlayer cp = manager.getClanPlayer(player);
            if(cp != null){
            
            
            String clan = cp.getClan().getName();
            if(arg[0].equalsIgnoreCase("players") || arg[0].equalsIgnoreCase("p") || arg[0].equalsIgnoreCase("player")){
                kosmanager.readPlayerKOS(player, cp, clans, false);
                return true;
            }
            if(arg[0].equalsIgnoreCase("clans") || arg[0].equalsIgnoreCase("clan") || arg[0].equalsIgnoreCase("c")){
                kosmanager.readClanKOS(player, cp, clans);
                return true;
            }
            if(arg[0].equalsIgnoreCase("more")){
                kosmanager.readPlayerKOS(player, cp, clans, true);
                return true;
            }
            if(arg[0].equalsIgnoreCase("add")){
                if(cp.isLeader()){
                    if(arg.length < 2){
                         player.sendMessage("§a[§bWarCraft§a] §fNot enouch arguments.");
                         return true;
                    }
                    if(arg[1].equalsIgnoreCase("clan") || arg[1].equalsIgnoreCase("c")){
                        kosmanager.addclanKOS(player, cp, arg[0], clans, true);
                        return true;
                    }
                    if(arg.length < 3){
                         player.sendMessage("§a[§bWarCraft§a] §fNot enouch arguments.");
                         return true;
                    }
                    if(arg[1].equalsIgnoreCase("player") || arg[1].equalsIgnoreCase("p")){
                        kosmanager.addclanKOS(player, cp, arg[2], clans, false);
                        return true;
                    }
                    
                }
                if(!cp.isLeader()){
                    player.sendMessage("§a[§bWarCraft§a] §fYou are not a clan leader and cannot do whatever it is that you are trying to.");
                return true;
                }
            }
            if(arg[0].equalsIgnoreCase("remove")){
                if(cp.isLeader()){
                    if(arg.length < 2){
                        player.sendMessage("§a[§bWarCraft§a] §fNot enouch arguments.");
                        return true;
                    }
                    kosmanager.removeclanKOS(player, cp, arg[1], clans);
                    return true;
                }
                if(!cp.isLeader()){
                    player.sendMessage("§a[§bWarCraft§a] §fYou are not a clan leader and cannot do whatever it is that you are trying to.");
                    return true;
                }
            }
            
            return true;}
        }
        invalidCommand(sender, paramString);
        return true;
    }

    @Override
    public void invalidCommand(CommandSender sender, String paramString) {
        sender.sendMessage("§a[§bWarCraft§a] §fProper KOS usage:");
        sender.sendMessage("    §b- §f/kos                        §aviews this help page");
        sender.sendMessage("    §b- §f/kos players|player|p       §aviews players in kos");
        sender.sendMessage("    §b- §f/kos clans|clan|c           §aviews clans in kos");
        sender.sendMessage("    §b- §f/kos add clan|player <name> §aadds to list");
        sender.sendMessage("    §b- §f/kos remove <name>          §aremoves from list");
    }
    
    
    
    
        
        
        
        
        
        
  }