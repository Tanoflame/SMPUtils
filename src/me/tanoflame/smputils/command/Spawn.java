package me.tanoflame.smputils.command;

import me.tanoflame.smputils.SMPUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn implements CommandExecutor {

    public SMPUtils plugin;

    public Spawn(SMPUtils plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.translateColorCodes(plugin.prefix + "$EMust be player to use this command"));
            return true;
        }
        Player player = (Player) sender;
        Location bed = player.getBedSpawnLocation();
        Location spawn = sender.getServer().getWorld("world").getSpawnLocation();
        if (bed != null) {
            player.sendMessage(plugin.translateColorCodes(plugin.prefix + "$SSending you to your spawnpoint"));
            player.teleport(bed);
        } else {
            player.sendMessage(plugin.translateColorCodes(plugin.prefix + "$SSending you to spawn"));
            player.teleport(spawn);
        }
        return true;
    }
}
