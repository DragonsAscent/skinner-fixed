package it.tigierrei.skinner.listeners;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;
import it.tigierrei.skinner.Skinner;
import it.tigierrei.skinner.entities.Mob;
import me.libraryaddict.disguise.DisguiseAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class BossSpawnListener implements Listener {

    @EventHandler
    public void MobSpawnEvent(MythicMobSpawnEvent e){
        final Entity entity = e.getEntity();
        Skinner.listaMobVivi.add(new Mob(entity, e.getMobType()));

        List<String> lista = (List<String>) Skinner.config.getList("Boss");
        for(final String nomeBoss : lista){
            if(e.getMobType().getInternalName().equalsIgnoreCase(nomeBoss)){
                Bukkit.getScheduler().runTaskLater(Skinner.pl, new Runnable(){
                    @Override
                    public void run(){
                        DisguiseAPI.disguiseToAll(entity, DisguiseAPI.getCustomDisguise(nomeBoss));
                    }
                },20L);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e){
        @SuppressWarnings("unchecked")
        List<String> lista = (List<String>) Skinner.config.getList("Boss");
        for(final Mob m : Skinner.listaMobVivi){
            for(final String nomeBoss : lista){
                if(m.getMythicMob().getInternalName().equalsIgnoreCase(nomeBoss)){
                    Bukkit.getScheduler().runTaskLater(Skinner.pl, new Runnable(){
                        @Override
                        public void run(){
                            DisguiseAPI.disguiseToPlayers(m.getEntity(), DisguiseAPI.getCustomDisguise(nomeBoss), e.getPlayer());
                        }
                    },20L);
                }
            }
        }
    }
}
