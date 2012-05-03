package com.warcraftserver.simpleclans.listeners;

import com.warcraftserver.simpleclans.Clan;
import com.warcraftserver.simpleclans.ClanPlayer;
import com.warcraftserver.simpleclans.SimpleClans;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * @author phaed
 */
public class SCEntityListener implements Listener
{
    private SimpleClans plugin;

    /**
     *
     */
    public SCEntityListener()
    {
        plugin = SimpleClans.getInstance();
    }

    /**
     * @param event
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDeath(EntityDeathEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            Player victim = (Player) event.getEntity();

            if (plugin.getSettingsManager().isBlacklistedWorld(victim.getLocation().getWorld().getName()))
            {
                return;
            }

            Player attacker = null;

            // find attacker

            EntityDamageEvent lastDamageCause = victim.getLastDamageCause();

            if (lastDamageCause instanceof EntityDamageByEntityEvent)
            {
                EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) lastDamageCause;

                if (entityEvent.getDamager() instanceof Player)
                {
                    attacker = (Player) entityEvent.getDamager();
                }
                else if (entityEvent.getDamager() instanceof Arrow)
                {
                    Arrow arrow = (Arrow) entityEvent.getDamager();

                    if (arrow.getShooter() instanceof Player)
                    {
                        attacker = (Player) arrow.getShooter();
                    }
                }
            }

            if (attacker != null && victim != null)
            {
                ClanPlayer acp = plugin.getClanManager().getCreateClanPlayer(attacker.getName());
                ClanPlayer vcp = plugin.getClanManager().getCreateClanPlayer(victim.getName());

                // record attacker kill

                // if victim doesn't have a clan or attacker doesn't have a clan, then the kill is civilian
                // if both have verified clans, check for rival or default to neutral

                if (vcp.getClan() == null || acp.getClan() == null || !vcp.getClan().isVerified() || !acp.getClan().isVerified())
                {
                    acp.addCivilianKill();
                    plugin.getStorageManager().insertKill(attacker, acp.getTag(), victim, "", "c");
                }
                else if (acp.getClan().isRival(vcp.getTag()))
                {
                    acp.addRivalKill();
                    plugin.getStorageManager().insertKill(attacker, acp.getTag(), victim, vcp.getTag(), "r");
                }
                else
                {
                    acp.addNeutralKill();
                    plugin.getStorageManager().insertKill(attacker, acp.getTag(), victim, vcp.getTag(), "n");
                }

                // record death for victim

                vcp.addDeath();
                plugin.getStorageManager().updateClanPlayer(vcp);
            }
        }
    }

    /**
     * @param event
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamage(EntityDamageEvent event)
    {
        if (event.isCancelled())
        {
            return;
        }

        Player attacker = null;
        Player victim = null;

        if (event instanceof EntityDamageByEntityEvent)
        {
            EntityDamageByEntityEvent sub = (EntityDamageByEntityEvent) event;

            if (sub.getEntity() instanceof Player && sub.getDamager() instanceof Player)
            {
                attacker = (Player) sub.getDamager();
                victim = (Player) sub.getEntity();
            }

            if (sub.getEntity() instanceof Player && sub.getDamager() instanceof Arrow)
            {
                Arrow arrow = (Arrow) sub.getDamager();

                if (arrow.getShooter() instanceof Player)
                {
                    attacker = (Player) arrow.getShooter();
                    victim = (Player) sub.getEntity();
                }
            }
        }

        if (victim != null)
        {
            if (plugin.getSettingsManager().isBlacklistedWorld(victim.getLocation().getWorld().getName()))
            {
                return;
            }
        }

        if (attacker != null && victim != null)
        {
            ClanPlayer acp = plugin.getClanManager().getClanPlayer(attacker);
            ClanPlayer vcp = plugin.getClanManager().getClanPlayer(victim);


            Clan vclan = vcp == null ? null : vcp.getClan();
            Clan aclan = acp == null ? null : acp.getClan();


            if (plugin.getSettingsManager().isPvpOnlywhileInWar())
            {
                // if one doesn't have clan then they cant be at war

                if (aclan == null || vclan == null)
                {
                    event.setCancelled(true);
                    return;
                }
                
                if (plugin.getPermissionsManager().has(victim, "simpleclans.mod.nopvpinwar") 
                        && attacker != null && victim != null) {
                    event.setCancelled(true);
                    return;
                }

                // if not warring no pvp

                if (!aclan.isWarring(vclan))
                {
                    event.setCancelled(true);
                    return;
                }
            }

            if (vclan != null)
            {
                if (aclan != null)
                {
                    // personal ff enabled, allow damage

                    if (vcp.isFriendlyFire())
                    {
                        return;
                    }

                    // clan ff enabled, allow damage

                    if (vclan.isFriendlyFire())
                    {
                        return;
                    }

                    // same clan, deny damage

                    if (vclan.equals(aclan))
                    {
                        event.setCancelled(true);
                        return;
                    }

                    // ally clan, deny damage

                    if (vclan.isAlly(aclan.getTag()))
                    {
                        event.setCancelled(true);
                        return;
                    }
                }
                else
                {
                    // not part of a clan - check if safeCivilians is set

                    if (plugin.getSettingsManager().getSafeCivilians())
                    {
                        event.setCancelled(true);
                        return;
                    }
                }
            }
            else
            {
                // not part of a clan - check if safeCivilians is set

                if (plugin.getSettingsManager().getSafeCivilians())
                {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}
