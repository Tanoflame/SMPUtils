package me.tanoflame.smputils.command.tpa;

import me.tanoflame.smputils.SMPUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.Map;

public class BackCommand implements CommandExecutor, Listener {

    public SMPUtils plugin;
    private Map<Player, Location> lastLocation = new HashMap<Player, Location>();

    public BackCommand(SMPUtils plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.translateColorCodes(plugin.prefix + "$EMust be player to use this command"));
            return true;
        }
        Player player = (Player) sender;
        if (!lastLocation.containsKey(player)) {
            player.sendMessage(plugin.translateColorCodes(plugin.prefix + "$EError: No location found"));
            return true;
        }
        Location loc = lastLocation.get(player);
        player.sendMessage(plugin.translateColorCodes(
                "$STeleporting back to X:$D" + loc.getBlockX() + " $SY:$D" + loc.getBlockY() + " $SZ:$D" + loc.getBlockZ()
        ));
        player.teleport(loc);

        return true;
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        PlayerTeleportEvent.TeleportCause cause = event.getCause();
        if (cause == PlayerTeleportEvent.TeleportCause.COMMAND || cause == PlayerTeleportEvent.TeleportCause.PLUGIN) {
            lastLocation.put(event.getPlayer(), event.getFrom());
        }
    }
}
