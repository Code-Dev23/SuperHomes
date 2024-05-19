package it.scopped.superhomes.utilities.teleport;

import it.scopped.superhomes.SuperHomes;
import it.scopped.superhomes.utilities.strings.CC;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

public class TeleportUtil {
    public static void executeTeleport(Player player, Location to, int seconds) {
        AtomicInteger time = new AtomicInteger(seconds);
        final double initialX = player.getLocation().getX();
        final double initialY = player.getLocation().getY();
        final double initialZ = player.getLocation().getZ();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (checkLocation(player.getLocation(), initialX, initialY, initialZ)) {
                    CC.send(player, "&cTi sei mosso! &7Teletrasporto annullato...");
                    CC.sendTitle(player, "&c&lTeletrasporto annullato", "&7Ti sei mosso!", 3);
                    cancel();
                    return;
                }
                if (time.get() == 0) {
                    player.teleport(to);
                    CC.send(player, "&aTeletrasportato con successo!");
                    this.cancel();
                    return;
                }
                CC.send(player, "&7Verrai teletrasportato tra %seconds% secondi...".replace("%seconds%", String.valueOf(time.get())));
                CC.sendTitle(player, "&6&lTeletrasporto in corso...", "&7%seconds%".replace("%seconds%", String.valueOf(time.get())), 2);
                time.decrementAndGet();
            }
        }.runTaskTimer(SuperHomes.getInstance(), 0, 20L);
    }

    private static boolean checkLocation(Location startLocation, double initialX, double initialY, double initialZ) {
        return startLocation.getX() != initialX || startLocation.getY() != initialY || startLocation.getZ() != initialZ;
    }
}