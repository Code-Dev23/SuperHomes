package it.scopped.superhomes.backend.objects;

import it.scopped.superhomes.SuperHomes;
import it.scopped.superhomes.utilities.locations.LocationUtil;
import it.scopped.superhomes.utilities.threads.Tasks;
import lombok.Data;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class PlayerHomeData {
    private String name;
    private UUID uuid;
    private Map<String, Location> homes;
    private int maxHomes;
    private int currentHomes;

    public PlayerHomeData(Player player) {
        this.name = player.getName();
        this.uuid = player.getUniqueId();
        this.maxHomes = SuperHomes.getInstance().getConfig().getInt("default_max_homes");
        this.homes = new HashMap<>();
        load(true);
    }

    public PlayerHomeData(UUID uuid) {
        this.uuid = uuid;
        this.maxHomes = SuperHomes.getInstance().getConfig().getInt("default_max_homes");
        this.name = (Bukkit.getPlayer(uuid) != null) ? Bukkit.getPlayer(uuid).getName() : Bukkit.getOfflinePlayer(uuid).getName();
        this.homes = new HashMap<>();
        load(true);
    }

    public void load(boolean async) {
        if (async)
            Tasks.run(this::load, true);
        else
            load();
    }

    public void save(boolean async) {
        if (async)
            Tasks.run(this::save, true);
        else
            save();
    }

    private void load() {
        Document document = SuperHomes.getInstance().getDatabaseManager().getPlayer(this.uuid);
        if (document != null) {
            if (this.name == null) this.name = document.getString("name");
            this.currentHomes = document.getInteger("current_homes", 0);
            this.maxHomes = document.getInteger("max_homes");

            Document homesDocument = (Document) document.get("homes");
            if (homesDocument != null) {
                for (Map.Entry<String, Object> entry : homesDocument.entrySet()) {
                    String homeName = entry.getKey();
                    String locationString = (String) entry.getValue();
                    Location location = LocationUtil.deserializeLocation(locationString);
                    this.homes.put(homeName, location);
                }
            } else {
                this.homes = new HashMap<>();
            }
        } else {
            this.homes = new HashMap<>();
            save();
        }
    }

    private void save() {
        Document document = SuperHomes.getInstance().getDatabaseManager().getPlayer(this.uuid);
        if (document == null) document = new Document();
        document.put("uuid", this.uuid.toString());
        document.put("name", this.name);
        document.put("max_homes", this.maxHomes);
        document.put("current_homes", this.homes.size());

        Document homesDocument = new Document();
        for (Map.Entry<String, Location> entry : homes.entrySet()) {
            String homeName = entry.getKey();
            Location location = entry.getValue();
            String locationString = LocationUtil.serializeLocation(location);
            homesDocument.put(homeName, locationString);
        }
        document.put("homes", homesDocument);

        SuperHomes.getInstance().getDatabaseManager().replacePlayer(this, document);
    }
}
