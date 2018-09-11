package it.tigierrei.skinner.entities;

import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.entity.Entity;

public class Mob {
    private Entity entity;
    private MythicMob mythicMob;

    public Mob(Entity entity, MythicMob mythicMob){
        this.entity = entity;
        this.mythicMob = mythicMob;
    }

    public Entity getEntity(){
        return entity;
    }

    public MythicMob getMythicMob(){
        return mythicMob;
    }

}