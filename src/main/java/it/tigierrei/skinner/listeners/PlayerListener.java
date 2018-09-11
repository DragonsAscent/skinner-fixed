package it.tigierrei.skinner.listeners;

import it.tigierrei.skinner.Skinner;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Iterator;
import java.util.UUID;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        Bukkit.getScheduler().runTaskLater(Skinner.pl, new Runnable(){
            @Override
            public void run(){
                for(UUID id : Skinner.uuidList) {
                    NPC npc = CitizensAPI.getNPCRegistry().getByUniqueId(id);
                    if(npc != null){
                        String npcName = npc.getName();
                        Iterator<String> iter = Skinner.nameList.keySet().iterator();
                        while(iter.hasNext()) {
                            String s = iter.next();
                            if(npcName.equalsIgnoreCase(s) || npc.getFullName().equalsIgnoreCase(s)) {
                                Disguise disguise = DisguiseAPI.getCustomDisguise(Skinner.nameList.get(s));
                                if(disguise != null)
                                    DisguiseAPI.disguiseToPlayers(npc.getEntity(), disguise, e.getPlayer());
                            }
                        }
                    }
                }
            }
        },Skinner.delay);
    }
}
