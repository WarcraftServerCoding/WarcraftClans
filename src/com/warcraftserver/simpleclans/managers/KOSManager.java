
package com.warcraftserver.simpleclans.managers;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import com.warcraftserver.simpleclans.Clan;
import com.warcraftserver.simpleclans.ClanPlayer;
import com.warcraftserver.simpleclans.SimpleClans;

public class KOSManager {
    
    HashMap<Player, Integer> index = new HashMap<Player, Integer>();
    Logger log = Logger.getLogger("Minecraft");
    ClanManager manager = SimpleClans.getInstance().getClanManager();
    SimpleClans plugin = SimpleClans.getInstance();
    

    
    public void readClanKOS(Player player,ClanPlayer cplayer, File file){
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
                                csb.append(" §2" + p.getName() + ",");
                            }
                            else{
                                csb.append(" §f" + cp.getName() + ",");
                            }


                        }
                        player.sendMessage(csb.toString());
                    }
                }
                line = reader.readLine();
            }
            reader.close();
        }
        catch(IOException ex){
            log.info("[SCAARON]" + ex.toString());
        }        
    }
    
    public void readPlayerKOS(Player player,ClanPlayer cplayer, File file, boolean more){
        String clantag = cplayer.getClan().getTag();
        
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while(line != null){
                String[] split = line.split(":");
                if(split[0].equalsIgnoreCase(clantag)){
                    String[] kosplayers = split[2].split(",");
                    if(index.containsKey(player)){
                        if(more){
                            if(kosplayers.length <= (index.get(player) + 1) *10  && !(  (((index.get(player) + 1) *10) - kosplayers.length) >= 10 )  ){
                                player.sendMessage("§a[§bWarCraft§a] §fYour Rivals or Kill on Sign List:");
                                player.sendMessage("  §bPlayers Page§a: " );
                                for(int i = 0 ; i < 10 && kosplayers.length > (((index.get(player)) *10)+i); i++){
                                    int num = ((index.get(player)) *10) + i;
                                    player.sendMessage(" -" + getColor(kosplayers[num]));
                                }
                                player.sendMessage("§7Type '/kos more' to view more.");
                                int newindex = index.get(player) + 1;
                                index.remove(player);
                                index.put(player, newindex);
                                return;
                            }
                            if((index.get(player) + 1) *10 > kosplayers.length){
                                player.sendMessage("§a[§bWarCraft§a] §fNo more KOS pages to display.");
                                index.remove(player);
                                index.put(player, 1);
                                return;
                            }
                        }
                        else{
                            player.sendMessage("§a[§bWarCraft§a] §fYour Rivals or Kill on Sign List:");
                            player.sendMessage("  §bPlayers Page§a: " );
                            for(int i = 0 ; i < 11 && i < kosplayers.length; i++){
                                
                                player.sendMessage(" -" + getColor(kosplayers[i]));
                                
                            }
                            index.remove(player);
                            index.put(player, 1);
                            return;
                        }
                    }
                    else{
                        player.sendMessage("§a[§bWarCraft§a] §fYour Rivals or Kill on Sign List:");
                        player.sendMessage("  §bPlayers Page§a: " );
                        for(int i = 0 ; i < 10; i++){
                               if(i < kosplayers.length){
                                player.sendMessage(" -" + getColor(kosplayers[i]));}
                                
                                
                            }
                    index.put(player, 1);
                    }
                    
                    
                    
                }
            line = reader.readLine();
            }
            reader.close();
        }
        catch(IOException ex){
            log.info("[SCAARON]" + ex.toString());
        }
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
        return ("§f" + player.getName());
    }
    
        public String getColor(String pname){
            Player player = Bukkit.getPlayer(pname);
            if(player != null){
                if(player.isOnline()){
                    return ("§2" + pname);
                }
            }
        return ("§f" + pname);
    }
    
    public void addclanKOS(Player player,ClanPlayer cplayer, String toAdd, File file, boolean isClan){
        String clantag = cplayer.getClan().getTag();
        StringBuilder toWrite = new StringBuilder();
        boolean exists = false;
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while(line != null){
                String[] split = line.split(":");
                if(split[0].equalsIgnoreCase(clantag)){
                    exists = true;
                    StringBuilder sb = new StringBuilder();
                    if(isClan){
                        sb.append(split[0]).append(":").append(split[1]).append(",").append(toAdd.toLowerCase()).append(":").append(split[2]).append("\n");
                    }
                    if(!isClan){
                        sb.append(split[0]).append(":").append(split[1]).append(":").append(split[2]).append(",").append(toAdd.toLowerCase()).append("\n");
                    }
                    toWrite.append(sb.toString());
                    player.sendMessage("§a[§bWarCraft§a] §fThe following has been added to KOS:");
                    player.sendMessage("    §b- §a" + toAdd);
                }
                else{
                    toWrite.append(line).append("\n");
                }
                line = reader.readLine();
            }
            
            if(!exists){
                if(isClan){
                    toWrite.append(clantag).append(":").append(toAdd.toLowerCase()).append(":").append("\n");
                }
                if(!isClan){
                    toWrite.append(clantag).append(":").append(":").append(toAdd.toLowerCase()).append("\n");
                }
                player.sendMessage("§a[§bWarCraft§a] §fThe following has been added to KOS:");
                player.sendMessage("    §b- §a" + toAdd);
            }
            reader.close();
            FileWriter writer = new FileWriter(file);
            writer.write(toWrite.toString());
            writer.close();
            
        }
        catch(IOException ex){
            log.info("[SCAARON]" + ex.toString());
        }
    }
    
    public void removeclanKOS(Player player, ClanPlayer cplayer, String toRemove, File file){
        StringBuilder sb = new StringBuilder();
        String clantag = cplayer.getClan().getTag();
        boolean exists = false;
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while(line != null){
                String[] split = line.split(":");
                if(split[0].equalsIgnoreCase(clantag)){
                    if(line.contains(toRemove.toLowerCase())){
                        line.replace("," + toRemove.toLowerCase(), "");
                        line.replace(toRemove.toLowerCase(), "");
                        exists = true;
                        player.sendMessage("§a[§bWarCraft§a] §fThe following has been removed from KOS:");
                        player.sendMessage("    §b- §a" + toRemove);
                    }
                }
                sb.append(line).append("\n");
                line = reader.readLine();
            }
            if(!exists){
                player.sendMessage("§a[§bWarCraft§a] §fYour clan does not have a KOS.");
            }
            reader.close();
            FileWriter writer = new FileWriter(file);
            writer.write(sb.toString());
            writer.close();
        }
        catch(IOException ex){
            log.info("[SCAARON]" + ex.toString());
        }
    }
    
}
