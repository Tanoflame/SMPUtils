package net.gravitydevelopment;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.tanoflame.smputils.SMPUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class GithubUpdater {

    private final SMPUtils plugin;

    public GithubUpdater(SMPUtils plugin) {
        this.plugin = plugin;
    }

    public void autoUpdate(){
        try {
            String version = SMPUtils.version; //My main class is called Main (Yes I know), it's also a static final so
//don't cry, you want to create a string in there and name your version v1.0.0 or something but it needs the v
//in front or it won't work.
            String parseVersion = version.replace(".", "");

            String tagname = null;
            URL api = new URL("https://api.github.com/repos/greeves12/COD/releases/latest"); //You want to set //this to your own project URL
            URLConnection con = api.openConnection();
            con.setConnectTimeout(15000);
            con.setReadTimeout(15000);

            JsonObject json = JsonParser.parseReader(new InputStreamReader(con.getInputStream())).getAsJsonObject();
            tagname = json.get("tag_name").getAsString();

            String parsedTagName = tagname.replace(".", ""); //Our new version number, normally it would look //like v1.0.0, however, it will be v100 now, basically it will determine which version is greater.

            int latestVersion = Integer.valueOf(parsedTagName.substring(1, parsedTagName.length()));

            URL download = new URL("https://github.com/greeves12/COD/releases/download/" + tagname + "/COD.jar"); //This is where you put your download URL for your latest release, be sure to change this to //your own.

            if(latestVersion > Integer.parseInt(parseVersion)) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN +"[COD] Found a new version " +ChatColor.RED+ tagname +ChatColor.LIGHT_PURPLE+ " downloading now!!");

                new BukkitRunnable(){

                    @Override
                    public void run() {
                        try {

                            InputStream in = download.openStream();
                            File temp = new File("plugins/update"); //We want to make the folder if it doesn't exist //already
                            if (!temp.exists()) {
                                temp.mkdir();
                            }
                            Path path = new File("plugins/update" + File.separator + "COD.jar").toPath(); //Here you will //put your file name, it must be named the same everytime for it to properly replace the existing, it will send //the file to the updates folder, from there the server will handle the rest.
                            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);

                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                }.runTaskLaterAsynchronously(plugin, 0); //We want this to be an async task so we don't clog up the main thread, also ThisPlugin.getPlugin() is just a short cut for me not to have to use this, it saves me time incase you were wondering
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
