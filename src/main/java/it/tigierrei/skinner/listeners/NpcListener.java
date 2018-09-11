package it.tigierrei.skinner.listeners;

import it.tigierrei.skinner.Skinner;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class NpcListener implements Listener {

    @EventHandler
    public void onNpcSpawn(final NPCSpawnEvent e) {
        //Controllo che l'uuid non sia già presente per evitare duplicati (il caricamento di un npc viene considerato spawn e ciò comporterebbe duplicati)
        Bukkit.getScheduler().runTaskLater(Skinner.pl, new Runnable(){
            @Override
            public void run(){
                NPC npc = e.getNPC();
                if(npc != null){
                    for(UUID id :Skinner.uuidList) {
                        if(id.toString().equalsIgnoreCase(e.getNPC().getUniqueId().toString())) {
                            return;
                        }
                    }
                    Skinner.pl.addUUID(e.getNPC().getUniqueId());
                    Disguise disguise = DisguiseAPI.getCustomDisguise(Skinner.nameList.get(npc.getName()));
                    if(disguise != null)
                        DisguiseAPI.disguiseToAll(npc.getEntity(),disguise);
                }
            }
        },Skinner.delay);
    }
}