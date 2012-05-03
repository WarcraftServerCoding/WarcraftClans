
package com.warcraftserver.simpleclans;

import com.warcraftserver.simpleclans.Clan;
import com.warcraftserver.simpleclans.ClanPlayer;
import com.warcraftserver.simpleclans.SimpleClans;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;


public class TitlesThread implements Runnable{
    Player pp;
    SpoutPlayer spsp;
    String tt;
    ClanPlayer cpcp;
    SimpleClans plugin;
    TitlesListener listener = new TitlesListener(plugin);
            
    public TitlesThread(Player player, SpoutPlayer splayer, ClanPlayer cp){
        pp = player;
        spsp = splayer;
        cpcp = cp;
        this.plugin = SimpleClans.getInstance();
    }
    
    public void run(){
        setTags(pp, spsp, cpcp);
    }
    
    public void setTags(Player player, SpoutPlayer splayer, ClanPlayer viewee){
        
        
        Player[] oPlayers = Bukkit.getOnlinePlayers();
        if(viewee == null){
            for(int i = 0 ; i < oPlayers.length ; i++){
                if(oPlayers[i] != player){
                    SpoutPlayer viewer = SpoutManager.getPlayer(oPlayers[i]);
                    splayer.setTitleFor(viewer, player.getName());
                }
            }
            
        }
        else{
            for(int i = 0 ; i < oPlayers.length ; i++){
                if(oPlayers[i] != player){
                    String tag = viewee.getTag() + ".";
                    SpoutPlayer viewer = SpoutManager.getPlayer(oPlayers[i]);
                    splayer.setTitleFor(viewer, tag + getStatusColor(oPlayers[i], player) + player.getName());
                }
            }
        }
  }
  
  public ChatColor getStatusColor(Player viewer, Player viewee ){
      
        Clan vr = listener.getClan(viewer);
        Clan ve = listener.getClan(viewee);
        if(ve != null && vr != null){
            if(ve.equals(vr)){
                return ChatColor.DARK_GREEN;
            }
            if(ve.isAlly(vr.toString())){
                return ChatColor.DARK_GREEN;
            }
            else if(ve.isRival(vr.toString())){
                return ChatColor.DARK_RED;
            }
            else{
                return ChatColor.DARK_BLUE;
            }
      }
      return ChatColor.WHITE;
  
  }
}
