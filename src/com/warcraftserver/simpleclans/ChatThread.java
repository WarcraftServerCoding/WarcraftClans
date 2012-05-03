/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.warcraftserver.simpleclans;

import java.util.logging.Logger;
import com.warcraftserver.simpleclans.ClanPlayer;
import com.warcraftserver.simpleclans.SimpleClans;
import com.warcraftserver.simpleclans.managers.ClanManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

/**
 *
 * @author Aaron Somers
 */
public class ChatThread implements Runnable{
    
    Player player;
    String message;
    Logger log = Logger.getLogger("Minecraft");
    SimpleClans plugin;
    TitlesListener listener = new TitlesListener(plugin);
//    int chatnum;
//    Titles titles = new Titles();
//    ClanManager cm = new ClanManager();
    
    public ChatThread(Player player, String message){
        this.player = player;
        this.message = message;
        this.plugin = SimpleClans.getInstance();
//        this.chatnum = chatnum;
    }
    
    @Override
    public void run() {
        SpoutPlayer sp = SpoutManager.getPlayer(player);
//        sp.setTitle(player.getName() + "\n" +ChatColor.YELLOW + message);
        Player[] oPlayers = Bukkit.getOnlinePlayers();
        ClanPlayer viewee = plugin.getClanManager().getClanPlayer(player);
        for(int i = 0 ; i < oPlayers.length ; i++){
            if(oPlayers[i] != player){
                SpoutPlayer viewer = SpoutManager.getPlayer(oPlayers[i]);
//                ClanPlayer cp = plugin.getClanManager().getClanPlayer(oPlayers[i]);
                if(viewee == null){
                    sp.setTitleFor(viewer, player.getName() + "\n" +ChatColor.YELLOW + message);}
                else{
                    String tag = viewee.getTag() + ".";
                    sp.setTitleFor(viewer, tag + player.getName() + "\n" +ChatColor.YELLOW + message);}
            }
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            log.severe("[Titles] Error in sleeping thread ChatThread. contact somers.");
        }
        
//        if(listener.getNum(player) > chatnum){
            
                Thread thread = new Thread(new TitlesThread(player, sp, viewee));
                thread.setPriority(java.lang.Thread.MAX_PRIORITY);
                thread.start();
            
//        }
    }
    
}
