package it.scopped.superhomes.listeners;

import it.scopped.superhomes.SuperHomes;
import it.scopped.superhomes.backend.objects.PlayerHomeData;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class GeneralListeners implements Listener {
    private final SuperHomes plugin;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerHomeData data = new PlayerHomeData(player);
        data.setMaxHomes(plugin.getCacheStorage().getMaxHomesOfPlayer(player));

        plugin.getCacheStorage().getLoadedHomes().put(player.getUniqueId(), new PlayerHomeData(player));
    }
}
