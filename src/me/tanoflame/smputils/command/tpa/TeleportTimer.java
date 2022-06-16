package me.tanoflame.smputils.command.tpa;

import me.tanoflame.smputils.SMPUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportTimer {

    private final SMPUtils plugin;
    public static int TaskID;
    private int time = 5;

    public TeleportTimer(SMPUtils plugin) {
        this.plugin = plugin;
    }

    public void startTimer(Player player, Player target) {
        TaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            @Override
            public void run() {
                if (time == 0) {
                    target.sendMessage(plugin.translateColorCodes(plugin.prefix + "$STeleporting to: $D" + target.getName()));
                    Location loc = player.getLocation();
                    target.teleport(loc);
                    stopTimer();
                    return;
                }

                target.sendMessage(plugin.translateColorCodes("$STeleporting in $D" + time + " $S..."));
                time--;
            }
        }, 0l, 20l);
    }

    public static void stopTimer() {
        Bukkit.getScheduler().cancelTask(TaskID);
    }
}
