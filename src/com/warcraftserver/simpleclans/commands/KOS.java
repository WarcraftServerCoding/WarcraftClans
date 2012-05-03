package com.warcraftserver.simpleclans.commands;

import java.text.MessageFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.io.*;
import java.util.logging.Logger;
//import java.lang.reflect.ArrayList;
import com.warcraftserver.simpleclans.ChatBlock;
import com.warcraftserver.simpleclans.Clan;
import com.warcraftserver.simpleclans.ClanPlayer;
import com.warcraftserver.simpleclans.Helper;
import com.warcraftserver.simpleclans.SimpleClans;
import com.warcraftserver.simpleclans.managers.ClanManager;
import com.warcraftserver.simpleclans.managers.PermissionsManager;
import com.warcraftserver.simpleclans.managers.RequestManager;
import com.warcraftserver.simpleclans.managers.SettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

public class KOS implements ExecutorInterface
{
    SimpleClans plugin = SimpleClans.getInstance();
    Logger log = Logger.getLogger("Minecraft");
    ClanManager manager = SimpleClans.getInstance().getClanManager();
    File clans = new File("plugins/SimpleClans/clankos.txt");
    File clanp = new File("plugins/SimpleClans/playerkos.txt");

    public String getStatus(Player cp, Player cpo){
        Clan cpclan = manager.getClanByPlayerName(cp.getName());
        Clan cpoclan = manager.getClanByPlayerName(cpo.getName());
        if(cpclan.isRival(cpoclan.toString())){
                return ", " + cpo.getName();
            }
        return "";
  }

    @Override
    public boolean onCommand(CommandSender sender, Command paramCommand, String paramString, String[] arg) {
        if(arg.length == 0){
            Player player = null;
            if(sender instanceof Player){
                player = (Player)sender;
            }
            ClanPlayer cp = manager.getClanPlayer(player);
            if(cp != null){
            
            
            String clan = cp.getClan().getName();
            readclanKOS(player, cp, clans);
            
            return true;}
        }
        invalidCommand(sender, paramString);
        return true;
    }

    @Override
    public void invalidCommand(CommandSender paramCommandSender, String paramString) {
        paramCommandSender.sendMessage("ERROR WITH KOS COMMAND");
    }
    
    
    
    public void readclanKOS(Player player,ClanPlayer cplayer, File file){
        String clantag = cplayer.getClan().getTag();
        player.sendMessage("§a[§bWarCraft§a] §fYour Rivals or Kill on Sign List:");
        player.sendMessage("  §bCLANS§a: " );
        StringBuilder psb = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while(line != null){
                String[] split = line.split(":");
                if(split[0].equalsIgnoreCase(clantag)){
                    String[] kosclans = split[1].split(",");
                    for(int c = 0 ; c < kosclans.length ; c++){
                        StringBuilder csb = new StringBuilder();
                        csb.append(kosclans[c]).append("§4 - ");
                        List<ClanPlayer> members = manager.getClan(kosclans[c]).getMembers();

                        for (ClanPlayer cp : members){
                            Player p = plugin.getServer().getPlayer(cp.getName());

                            boolean isOnline = false;

                            if (p != null){
                                csb.append("§2" + p.getName());
                            }
                            else{
                                csb.append("§f" + cp.getName());
                            }


                        }
                        player.sendMessage(csb.toString());
                    }
                    String[] players = split[2].split(",");
                    player.sendMessage("  §bPLAYERS§a: " );
                    psb.append("  §a-");
                    for(int p = 0 ; p < players.length ; p++){
                        psb.append(getColor(players[p]));
                    }
                    player.sendMessage(psb.toString());
                    
                    
                }
                line = reader.readLine();
            }
            reader.close();
        }
        catch(IOException ex){
            log.info("[SCAARON]" + ex.toString());
        }
        
//        player.sendMessage(psb.toString());
        
    }
    
    public void addKOS(String name, File file, Clan clan, Player player){
    
    }
    
    public void removeKOS(String name, File file, Clan clan, Player player){
        
    }
    
    public String getColor(ClanPlayer player){

           Player p = player.toPlayer();
            if(p != null){
                if(p.isOnline()){
                    return ("§2" + p.getName());
                }
            }
        
        return ("§f" + player.getName());
    }
    
    public String getColor(Player player){
            if(player != null){
                if(player.isOnline()){
                    return ("§2" + player.getName());
                }
            }
//        }
        return ("§f" + player.getName());
    }
    
        public String getColor(String pname){
            Player player = Bukkit.getPlayer(pname);
            if(player != null){
                if(player.isOnline()){
                    return ("§2" + pname);
                }
            }
//        }
        return ("§f" + pname);
    }
    
    public void addclanKOS(Player player,ClanPlayer cplayer, File file, boolean isClan){
        String clantag = cplayer.getClan().getTag();
        player.sendMessage("§a[§bWarCraft§a] §fYour Rivals or Kill on Sign List:");
        player.sendMessage("  §bCLANS§a: " );
        StringBuilder psb = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while(line != null){
                String[] split = line.split(":");
            }
        }
        catch(IOException ex){
            log.info("[SCAARON]" + ex.toString());
        }
    }
    
    public void removeclanKOS(Player player, String variable, String name, File file, boolean isClan){
        StringBuilder sb = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while(line != null){
                String[] split = line.split(":");
                if(split[0].equalsIgnoreCase(name)){
                    if(line.contains(variable.toLowerCase())){
                        line.replace(variable.toLowerCase(), "");
                    }
                }
                sb.append(line).append("\n");
            }
        }
        catch(IOException ex){
            log.info("[SCAARON]" + ex.toString());
        }
    }
        
        
        
        
        
        
  }