package it.tigierrei.skinner;

import it.tigierrei.skinner.commands.SkinnerCommands;
import it.tigierrei.skinner.entities.Mob;
import it.tigierrei.skinner.listeners.BossSpawnListener;
import it.tigierrei.skinner.listeners.NpcListener;
import it.tigierrei.skinner.listeners.PlayerListener;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class Skinner extends JavaPlugin {

    //Hashmap con chiave il nome dell'NPC e come valore il nome del suo disguise
    public static HashMap<String, String> nameList;

    //Lista degli UUID degli NPC
    public static List<UUID> uuidList;

    //Delay aggiornamento skin NPC
    public static long delay;

    //Logger
    public Logger log = getLogger();

    //Carico il file del config
    private File configurationFile = new File(getDataFolder(),"config.yml");
    public static FileConfiguration config;

    public static List<Mob> listaMobVivi;

    public static Skinner pl;

    @Override
    public void onEnable(){
        //Creo un'istanza del plugin
        pl = this;

        //Creo/carico il config
        setupConfig();

        //Inizializzo la lista dei mob vivi di MythicMobs
        listaMobVivi = new ArrayList<>();

        //Registro i listeners
        if(config.getBoolean("MythicMobs")) {
            getServer().getPluginManager().registerEvents(new BossSpawnListener(), this);
        }

        if(config.getBoolean("Citizens")){
            getServer().getPluginManager().registerEvents(new NpcListener(), this);
            getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        }

        //Registro i comandi
        this.getCommand("sk").setExecutor(new SkinnerCommands());
        this.getCommand("skinner").setExecutor(new SkinnerCommands());

        //Imposto il timer per aggiornare le skin degli NPC
        Bukkit.getScheduler().runTaskTimer(Skinner.pl, new Runnable(){
            @Override
            public void run(){
                updateSkins();
            }
        },600L,600L);
    }

    @Override
    public void onDisable(){

    }

    private void killMobs(){
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        Bukkit.dispatchCommand(console, "mm mobs killall");
    }

    /**
     * Se esiste il file di configurazione ne carica le informazioni,
     * altrimenti lo crea ed imposta informazioni di default
     */
    public void setupConfig() {
        File plFolder = getDataFolder();
        if(!plFolder.exists()) {
            plFolder.mkdirs();
        }
        if(!configurationFile.exists()) {
            saveDefaultConfig();
            config = YamlConfiguration.loadConfiguration(configurationFile);
            config.set("MythicMobs",false);
            config.set("Citizens",false);
            config.set("Boss", new ArrayList<String>(Arrays.asList("Default")));
            config.set("Names", config.createSection("Names"));
            config.set("Delay", 20L);
            config.getConfigurationSection("Names").set("npc name", "skin name");
            config.set("UUID", new ArrayList<String>(Arrays.asList(UUID.randomUUID().toString())));
            saveConfig();
        }
        loadConfig();
    }

    private void loadConfig() {
        uuidList = new ArrayList<>();
        nameList = new HashMap<>();
        config = YamlConfiguration.loadConfiguration(configurationFile);
        ConfigurationSection section = config.getConfigurationSection("Names");
        Iterator<?> iter = section.getKeys(false).iterator();
        while(iter.hasNext()) {
            String key = (String) iter.next();
            String name = (String)section.get(key);
            nameList.put(key, name);
        }
        List<String> uuidStringList = config.getStringList("UUID");

        for(String s : uuidStringList) {
            uuidList.add(UUID.fromString(s));
        }
        delay = config.getLong("Delay");
    }

    public void saveConfig() {
        try {
            config.save(configurationFile);
        } catch (IOException e) {
            log.warning("Error while saving the configuration file");
            e.printStackTrace();
        }
    }

    public void addUUID(UUID id) {
        for(UUID x : uuidList) {
            if(x.toString().equalsIgnoreCase(id.toString()))
                return;
        }
        uuidList.add(id);
        List<String> l = config.getStringList("UUID");
        l.add(id.toString());
        config.set("UUID", l);
        saveConfig();
        loadConfig();
    }

    public UUID removeUUID(UUID uuid) {
        for(int i = 0; i < uuidList.size(); i++){
            if(uuidList.get(i).compareTo(uuid) == 0) {
                return uuidList.remove(i);
            }
        }
        return null;
    }

    private void updateSkins() {
        for(UUID id : Skinner.uuidList) {
            NPC npc = CitizensAPI.getNPCRegistry().getByUniqueId(id);
            if(npc != null){
                Disguise disguise = DisguiseAPI.getCustomDisguise(Skinner.nameList.get(npc.getName()));
                if(disguise != null)
                    DisguiseAPI.disguiseToAll(npc.getEntity(),disguise);
            }
        }
    }
}
