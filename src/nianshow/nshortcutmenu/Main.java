package nianshow.nshortcutmenu;

import nianshow.nshortcutmenu.listener.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Nianshow
 */
public class Main extends JavaPlugin{
    public static Plugin plugin;

    @Override
    public void onEnable(){
        plugin = this;
        // 输出自己的大名
        Bukkit.getConsoleSender().sendMessage("§6NShortcutMenu§r: {");
        Bukkit.getConsoleSender().sendMessage("  作者: §e粘兽§r,");
        Bukkit.getConsoleSender().sendMessage("  版本: §e"+getDescription().getVersion()+"§r,");
        boolean depend = Bukkit.getPluginManager().isPluginEnabled("VexView");
        // 没有VexView将不工作
        if(depend){
            Bukkit.getConsoleSender().sendMessage("  状态: §2true");
            Bukkit.getConsoleSender().sendMessage("}");
            Bukkit.getPluginManager().registerEvents(new PlayerListener(),this);
        }else{
            Bukkit.getConsoleSender().sendMessage("  状态: §4false§r,");
            Bukkit.getConsoleSender().sendMessage("  原因: §c插件需要§bVexView§c作为前置§r");
            Bukkit.getConsoleSender().sendMessage("}");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }
}
