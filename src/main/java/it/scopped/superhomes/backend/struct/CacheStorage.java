package it.scopped.superhomes.backend.struct;

import com.google.common.collect.Maps;
import it.scopped.superhomes.SuperHomes;
import it.scopped.superhomes.backend.objects.PlayerHomeData;
import it.scopped.superhomes.utilities.threads.Tasks;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.Map;
import java.util.UUID;

@Getter
public class CacheStorage {
    private final SuperHomes plugin;

    @Getter
    private final Map<UUID, PlayerHomeData> loadedHomes = Maps.newConcurrentMap();

    public CacheStorage(SuperHomes plugin) {
        this.plugin = plugin;

        Tasks.runTimer(this::saveData, 0, 20 * 20L, true);
    }

    public PlayerHomeData getByUuid(UUID uuid) {
        PlayerHomeData data = loadedHomes.get(uuid);

        if (data == null) {
            data = new PlayerHomeData(uuid);
            data.setMaxHomes(getMaxHomesOfPlayer(Bukkit.getPlayer(uuid)));
            data.load(true);
        }
        data.setMaxHomes(getMaxHomesOfPlayer(Bukkit.getPlayer(uuid)));
        return data;
    }

    public PlayerHomeData getByName(String name) {
        return getByUuid((Bukkit.getPlayer(name) == null) ? Bukkit.getOfflinePlayer(name).getUniqueId() : Bukkit.getPlayer(name).getUniqueId());
    }

    public Location getHomeLocation(Player player, String home) {
        PlayerHomeData data = getByUuid(player.getUniqueId());
        if(data == null)
            return null;

        return data.getHomes().get(home);
    }

    public void createHome(Player player, String home) {
        PlayerHomeData data = getByUuid(player.getUniqueId());
        if(data == null)
            return;
        data.getHomes().put(home, player.getLocation());
        data.setCurrentHomes(data.getCurrentHomes() + 1);
    }
    public void deleteHome(Player player, String home) {
        PlayerHomeData data = getByUuid(player.getUniqueId());
        if(data == null)
            return;
        data.getHomes().remove(home);
        data.setCurrentHomes(data.getCurrentHomes() - 1);
    }

    public boolean existHome(Player player, String home) {
        PlayerHomeData data = getByUuid(player.getUniqueId());
        if(data == null)
            return false;
        return data.getHomes().containsKey(home);
    }

    public int getMaxHomesOfPlayer(Player player) {
        int maxHomes = plugin.getConfig().getInt("default_max_homes");
        for (PermissionAttachmentInfo perm : player.getEffectivePermissions()) {
            if (perm.getPermission().startsWith("superhomes.max_homes."))
                maxHomes = Integer.parseInt(perm.getPermission().toLowerCase().replaceAll("superhomes.max_homes.", ""));
        }
        return maxHomes;
    }

    public boolean reachedMaxHomes(Player player) {
        PlayerHomeData data = getByUuid(player.getUniqueId());
        System.out.println("DATA TEST IF IS VALID");
        System.out.println(data);
        if(data == null)
            return false;
        System.out.println("REACHED?");
        System.out.println(data.getCurrentHomes() >= data.getMaxHomes());
        return data.getCurrentHomes() >= data.getMaxHomes();
    }

    public void saveData() {
        loadedHomes.values().forEach(data -> data.save(true));
    }
}
