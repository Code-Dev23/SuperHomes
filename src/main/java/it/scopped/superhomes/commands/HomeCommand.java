package it.scopped.superhomes.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import it.scopped.superhomes.SuperHomes;
import it.scopped.superhomes.backend.objects.PlayerHomeData;
import it.scopped.superhomes.utilities.strings.CC;
import it.scopped.superhomes.utilities.teleport.TeleportUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@RequiredArgsConstructor
@CommandAlias("home")
public class HomeCommand extends BaseCommand {
    private final SuperHomes plugin;

    @Default
    public void onCommand(Player player, String[] args) {
        if(args.length == 0) {
            CC.send(player, "&cInserisci il nome della home.");
            return;
        }
        String home = args[0];
        if(!plugin.getCacheStorage().existHome(player, home)) {
            CC.send(player, "&cNon e' stata trovata nessuna home con questo nome.");
            return;
        }
        Location loc = plugin.getCacheStorage().getHomeLocation(player, home);
        if(loc == null) {
            CC.send(player, "&cNon e' stata trovata la posizione di questa home.");
            return;
        }
        TeleportUtil.executeTeleport(player, loc, 5);
        CC.send(player, "&aTeletrasporto avviato...");
    }

    @Subcommand("create")
    public void onSubCreate(Player player, String[] args) {
        if(args.length == 0) {
            CC.send(player, "&cInserisci il nome della home.");
            return;
        }
        String home = args[0];
        if(plugin.getCacheStorage().reachedMaxHomes(player)) {
            CC.send(player, "&cHai raggiunto il limite di home creabili.");
            return;
        }
        if(plugin.getCacheStorage().existHome(player, home)) {
            CC.send(player, "&cEsiste gia' una home con questo nome.");
            return;
        }
        plugin.getCacheStorage().createHome(player, home);
        CC.send(player, "&aHome creata con successo.");
    }

    @Subcommand("delete")
    public void onSubDelete(Player player, String[] args) {
        if(args.length == 0) {
            CC.send(player, "&cInserisci il nome della home.");
            return;
        }
        String home = args[0];
        if(!plugin.getCacheStorage().existHome(player, home)) {
            CC.send(player, "&cNon e' stata trovata nessuna home con questo nome.");
            return;
        }
        plugin.getCacheStorage().deleteHome(player, home);
        CC.send(player, "&aHome eliminata con successo.");
    }

    @Subcommand("list")
    public void onSubList(Player player) {
        PlayerHomeData data = plugin.getCacheStorage().getByUuid(player.getUniqueId());
        if(data == null)
            return;
        if(data.getHomes().isEmpty()) {
            CC.send(player, "&cAl momento non possiedi delle home.");
            return;
        }
        CC.send(player, "&b&lHome List");
        data.getHomes().keySet().forEach(home -> CC.send(player, "&7- &b%home%".replace("%home%", home)));
    }
}
