package me.tanoflame.smputils;

import me.tanoflame.smputils.command.*;
import me.tanoflame.smputils.command.tpa.BackCommand;
import me.tanoflame.smputils.command.tpa.TpaCommand;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class SMPUtils extends JavaPlugin {

    public final static String version = "0.1";

    public String prefix = "$D&l[$S&lSMP Utils$D&l]&r ";
    private final static int CENTER_PX = 154;
    private String stringColor = "&b";
    private String dataColor = "&f";
    private String errorColor = "&c";

    public TpaCommand tpa;

    @Override
    public void onEnable() {
        Bukkit.addRecipe(getStringRecipe());
        this.getCommand("spawn").setExecutor(new Spawn(this));
        this.getCommand("worldlist").setExecutor(new WorldList(this));
        this.getCommand("entitylist").setExecutor(new EntityList(this));

        tpa = new TpaCommand(this);
        this.getCommand("tpa").setExecutor(tpa);
        this.getCommand("tpaccept").setExecutor(tpa);
        this.getCommand("tpadeny").setExecutor(tpa);
        this.getCommand("tpacancel").setExecutor(tpa);

        CommandHelper helper = new CommandHelper(this);
        this.getCommand("tpa").setTabCompleter(helper);
        this.getCommand("tpaccept").setTabCompleter(helper);
        this.getCommand("tpadeny").setTabCompleter(helper);
        this.getCommand("tpacancel").setTabCompleter(helper);

        BackCommand bc = new BackCommand(this);
        this.getCommand("back").setExecutor(bc);
        this.getServer().getPluginManager().registerEvents(bc, this);
    }

    @Override
    public void onDisable() {

    }

    public ShapelessRecipe getStringRecipe() {
        ItemStack item = new ItemStack(Material.STRING, 4);

        NamespacedKey key = new NamespacedKey(this, "string");

        ShapelessRecipe recipe = new ShapelessRecipe(key, item);

        RecipeChoice.MaterialChoice choice = new RecipeChoice.MaterialChoice(Tag.WOOL);
        recipe.addIngredient(choice);

        return recipe;
    }

    public String translateColorCodes(String string) {
        String newStr = string.replace("$S", stringColor).replace("$D", dataColor).replace("$E", errorColor);
        return ChatColor.translateAlternateColorCodes('&', newStr);
    }

    public void sendCenteredMessage(Player player, String message){
        if(message == null || message.equals("")) player.sendMessage("");
        message = translateColorCodes(message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()){
            if(c == '§'){
                previousCode = true;
                continue;
            }else if(previousCode == true){
                previousCode = false;
                if(c == 'l' || c == 'L'){
                    isBold = true;
                    continue;
                }else isBold = false;
            }else{
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }
        player.sendMessage(sb.toString() + message);
    }
}
