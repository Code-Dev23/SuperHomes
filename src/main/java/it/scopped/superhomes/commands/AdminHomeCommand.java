package it.scopped.superhomes.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import it.scopped.superhomes.SuperHomes;
import it.scopped.superhomes.utilities.strings.CC;
import it.scopped.superhomes.utilities.teleport.TeleportUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@CommandAlias("adminhome|ahome")
@CommandPermission("superhomes.command.admin")
@RequiredArgsConstructor
public class AdminHomeCommand extends BaseCommand {
    private final SuperHomes plugin;

    @Default
    public void onCommand(Player player) {
        CC.send(player, "&cUtilizza: /adminhome <visit/delete> <player> <home>");
    }

    @Subcommand("visit")
    public void onSubVisit(Player player, String[] args) {
        if(args.length <= 1) {
            CC.send(player, "&cUtilizza: /adminhome <visit/delete> <player> <home>");
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if(target == null) {
            CC.send(player, "&cIl giocatore non e' stato trovato.");
            return;
        }
        String home = args[1];
        if(!plugin.getCacheStorage().existHome(target, home)) {
            CC.send(player, "&cNon e' stata trovata nessuna home per questo player.");
            return;
        }
        Location loc = plugin.getCacheStorage().getHomeLocation(player, home);
        if(loc == null) {
            CC.send(player, "&cNon e' stata trovata la posizione di questa home.");
            return;
        }
        player.teleport(loc);
        CC.send(player, "&aTeletrasportato con successo...");
    }

    @Subcommand("delete")
    public void onSubDelete(Player player, String[] args) {
        if(args.length <= 1) {
            CC.send(player, "&cUtilizza: /adminhome <visit/delete> <player> <home>");
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if(target == null) {
            CC.send(player, "&cIl giocatore non e' stato trovato.");
            return;
        }
        String home = args[1];
        if(!plugin.getCacheStorage().existHome(target, home)) {
            CC.send(player, "&cNon e' stata trovata nessuna home per questo player.");
            return;
        }

        plugin.getCacheStorage().deleteHome(target, home);
        CC.send(player, "&aHome eliminato per questo giocatore!");
    }
}