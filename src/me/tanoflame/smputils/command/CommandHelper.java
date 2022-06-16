package me.tanoflame.smputils.command;

import me.tanoflame.smputils.SMPUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandHelper implements TabCompleter {

    public SMPUtils plugin;

    public CommandHelper(SMPUtils plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> result = new ArrayList<String>();

        if (cmd.getName().equals("tpa")) {
            for (Player p: sender.getServer().getOnlinePlayers()) {
                if (args.length == 1) {
                    if (p.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                        result.add(p.getName());
                    }
                } else {
                    result.add(p.getName());
                }
            }
        } else if (cmd.getName().equals("tpaccept") || cmd.getName().equals("tpdeny")) {
            if (args.length == 1) {
                HashMap<Player, ArrayList<Player>> requests = plugin.tpa.requests;
                if (!requests.containsKey((Player) sender)) return result;

                ArrayList<Player> playerRequests = requests.get((Player) sender);
                for (Player p: playerRequests) {
                    if (p.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                        result.add(p.getName());
                    }
                }
            }
        }
        return result;
    }
}
