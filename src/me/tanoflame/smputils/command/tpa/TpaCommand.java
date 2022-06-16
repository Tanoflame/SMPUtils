package me.tanoflame.smputils.command.tpa;

import me.tanoflame.smputils.SMPUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class TpaCommand implements CommandExecutor {

    public SMPUtils plugin;
    public HashMap<Player, ArrayList<Player>> requests = new HashMap<Player, ArrayList<Player>>();
    public HashMap<Player, Player> cancelInfo = new HashMap<Player, Player>();

    public TpaCommand(SMPUtils plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.translateColorCodes(plugin.prefix + "$EMust be player to use this command"));
            return true;
        }
        Player player = (Player) sender;
        if (cmd.getName().equals("tpa")) {
            if (args.length == 0) {
                player.sendMessage(plugin.translateColorCodes(plugin.prefix + "$EUsage: /tpa <Player>"));
                return true;
            }
            if (args[0].equals(player.getName())) {
                player.sendMessage(plugin.translateColorCodes(plugin.prefix + "$EError: Unable to send request to self"));
                return true;
            }
            Player target = plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(plugin.translateColorCodes(plugin.prefix + "$EError: $D" + args[0] + " is an invalid player"));
                return true;
            } else {
                boolean result = registerRequest(player, target);
                if (result) {
                    player.sendMessage(plugin.translateColorCodes(plugin.prefix + "$SRequest sent to $D" + args[0]));
                    target.sendMessage(plugin.translateColorCodes(plugin.prefix + "$D" + player.getName() + " $Sis requesting to teleport to you"));
                    target.sendMessage(plugin.translateColorCodes("$STo accept use: $D/tpaccept"));
                    target.sendMessage(plugin.translateColorCodes("$Sor use: $D/tpaccept " + player.getName()));
                } else {
                    player.sendMessage(plugin.translateColorCodes(plugin.prefix + "$EA request was already sent to $D" + args[0]));
                }
                return true;
            }
        } else if (cmd.getName().equals("tpaccept")) {
            if (!requests.containsKey(player) || requests.get(player).size() == 0) {
                player.sendMessage(plugin.translateColorCodes(plugin.prefix + "$EError: No requests to you found"));
                return true;
            }
            ArrayList<Player> playerRequests = requests.get(player);
            if (args.length == 0) {
                Player target = playerRequests.get(playerRequests.size() - 1);
                player.sendMessage(plugin.translateColorCodes(plugin.prefix + "$STeleport request by $D" + target.getName() + " $Saccepted"));
                target.sendMessage(plugin.translateColorCodes(plugin.prefix + "$STeleport request to $D" + player.getName() + " $Saccepted"));
                acceptRequest(player, target);
                return true;
            } else {
                Player target = plugin.getServer().getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage(plugin.translateColorCodes(plugin.prefix + "$EError: $D" + args[0] + " $Sis an invalid player"));
                    return true;
                } else if (playerRequests.contains(target)) {
                    player.sendMessage(plugin.translateColorCodes(plugin.prefix + "$STeleport request by $D" + target.getName() + " $Saccepted"));
                    target.sendMessage(plugin.translateColorCodes(plugin.prefix + "$STeleport request to $D" + player.getName() + " $Saccepted"));
                    acceptRequest(player, target);
                    return true;
                } else {
                    player.sendMessage(plugin.translateColorCodes(plugin.prefix + "$D" + args[0] + " $Shas not sent a request"));
                    return true;
                }
            }
        } else if (cmd.getName().equals("tpadeny")) {
            if (!requests.containsKey(player) || requests.get(player).size() == 0) {
                player.sendMessage(plugin.translateColorCodes(plugin.prefix + "$EError: No requests to you found"));
                return true;
            }
            ArrayList<Player> playerRequests = requests.get(player);
            if (args.length == 0) {
                Player target = playerRequests.get(playerRequests.size() - 1);
                player.sendMessage(plugin.translateColorCodes(plugin.prefix + "$STeleport request by $D" + target.getName() + " $Sdenied"));
                target.sendMessage(plugin.translateColorCodes(plugin.prefix + "$STeleport request to $D" + player.getName() + " $Sdenied"));
                denyRequest(player, target);
                return true;
            } else {
                Player target = plugin.getServer().getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage(plugin.translateColorCodes(plugin.prefix + "$EError: $D" + args[0] + " $Sis an invalid player"));
                    return true;
                } else if (playerRequests.contains(target)) {
                    player.sendMessage(plugin.translateColorCodes(plugin.prefix + "$STeleport request by $D" + target.getName() + " $Sdenied"));
                    target.sendMessage(plugin.translateColorCodes(plugin.prefix + "$STeleport request to $D" + player.getName() + " $Sdenied"));
                    denyRequest(player, target);
                    return true;
                } else {
                    player.sendMessage(plugin.translateColorCodes(plugin.prefix + "$D" + args[0] + " $Shas not sent a request"));
                    return true;
                }
            }
        } else if (cmd.getName().equals("tpacancel")) {
            if (!cancelInfo.containsKey(player)) {
                player.sendMessage(plugin.translateColorCodes(plugin.prefix + "$EError: No teleport request found"));
                return true;
            }
            if (args.length == 0) {
                Player target = cancelInfo.get(player);
                ArrayList<Player> playerRequests = requests.get(target);
                playerRequests.remove(player);
                cancelInfo.remove(player);
                target.sendMessage(plugin.translateColorCodes(plugin.prefix + "$STeleport request by $D" + player.getName() + " $Shas been cancelled"));
                player.sendMessage(plugin.translateColorCodes(plugin.prefix + "$STeleport request to $D" + target.getName() + " $Shas been cancelled"));
                return true;
            } else {
                player.sendMessage(plugin.translateColorCodes(plugin.prefix + "$EError: Too many arguments"));
            }
        }
        return true;
    }

    private boolean registerRequest(Player player, Player target) {
        ArrayList<Player> playerRequests;
        if (requests.containsKey(target)) {
            playerRequests = requests.get(target);
        } else {
            playerRequests = new ArrayList<Player>();
        }

        if (playerRequests.contains(player)) return false;

        playerRequests.add(player);
        requests.put(target, playerRequests);
        cancelInfo.put(player, target);
        return true;
    }

    private void acceptRequest(Player player, Player target) {
        ArrayList<Player> playerRequests = requests.get(player);
        TeleportTimer run = new TeleportTimer(plugin);
        run.startTimer(player, target);
        playerRequests.remove(target);
        cancelInfo.remove(target);
    }

    private boolean denyRequest(Player player, Player target) {
        if (!requests.containsKey(player)) return false;
        ArrayList<Player> playerRequests = requests.get(player);

        if (!playerRequests.contains(target)) return false;

        playerRequests.remove(target);
        requests.put(player, playerRequests);
        cancelInfo.remove(target);
        return true;
    }
}
