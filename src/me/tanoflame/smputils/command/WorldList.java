package me.tanoflame.smputils.command;

import me.tanoflame.smputils.SMPUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldList implements CommandExecutor {

    private SMPUtils plugin;

    public WorldList(SMPUtils plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.translateColorCodes(plugin.prefix + "$EMust be player to use this command"));
            return true;
        }
        Player player = (Player) sender;
        plugin.sendCenteredMessage(player, plugin.prefix + "$SWorldList");
        for (World world: Bukkit.getServer().getWorlds()) {
            player.sendMessage(plugin.translateColorCodes(
                    "$S" + world.getName() + ": $D" + world.getPlayers().size() + " $SPlayer(s)"
            ));
        }
        return true;
    }
}
