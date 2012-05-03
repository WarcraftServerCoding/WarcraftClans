
package com.warcraftserver.simpleclans;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;
import com.warcraftserver.simpleclans.Clan;
import com.warcraftserver.simpleclans.SimpleClans;
import com.warcraftserver.simpleclans.ClanPlayer;
import com.warcraftserver.simpleclans.managers.ClanManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.EventHandler;
import org.getspout.spoutapi.player.SpoutPlayer;
import org.getspout.spoutapi.SpoutManager;

public class TitlesListener implements Listener{
    
    SimpleClans plugin;
    SimpleClans sc = SimpleClans.getInstance();
    Logger log = Logger.getLogger("Minecraft");
    HashMap<Player, Clan> clanmap = new HashMap<Player, Clan>();
    HashMap<Player, Integer> chatcount = new HashMap<Player, Integer>();
    
//    ClanPlayer clanplayer = null;
    
    public TitlesListener(SimpleClans plugin){
        this.plugin = SimpleClans.getInstance();
    }
    
    @EventHandler()
    public void onLogin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);
        SpoutPlayer sp = SpoutManager.getPlayer(player);
//        if(!chatcount.containsKey(player)){
//            chatcount.put(player, 0);
//        }
        if(cp != null){
            Clan clan = cp.getClan();
            addClan(player, clan);
            Thread thread = new Thread(new TitlesThread(player, sp, cp));
            thread.setPriority(java.lang.Thread.MAX_PRIORITY);
            thread.start();}
    }
    
    @EventHandler()
    public void onChat(PlayerChatEvent event){
        
        String message = event.getMessage();
        Player player = event.getPlayer();
//        int num = chatcount.get(player);
//        chatcount.remove(player);
//        chatcount.put(player, num + 1);
        Thread thread = new Thread(new ChatThread(player, message));
        thread.setPriority(java.lang.Thread.MAX_PRIORITY);
        thread.start();
    
    }
    
    public void addClan(Player cp, Clan clan){
        if(!clanmap.containsKey(cp)){
            clanmap.put(cp, clan);
            return;
        }
        if(clanmap.containsKey(cp)){
            clanmap.remove(cp);
            clanmap.put(cp, clan);
            return;
        }
    }
    
    public Clan getClan(Player cp){
        return clanmap.get(cp);
    }

    public Integer getNum(Player player){
        return chatcount.get(player);
    }
    
    
}
