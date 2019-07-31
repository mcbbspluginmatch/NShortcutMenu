package nianshow.nshortcutmenu.utils;

import nianshow.nshortcutmenu.cache.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Nianshow
 */
public class FileUtils{
    /**
     * 重置配置文件缓存
     * @param plugin 插件实例
     */
    public static void reloadCache(Plugin plugin){
        getCacheFromRes(plugin);
        getCacheFromMenu(plugin);
        getCacheFromMenuFolder(plugin);
    }

    /**
     * 缓存ResidenceMenu配置文件
     * @param plugin 插件实例
     */
    private static void getCacheFromRes(Plugin plugin){
        File file = new File(plugin.getDataFolder(),"ResidenceMenu.yml");
        if(!file.exists()){
            plugin.saveResource("ResidenceMenu.yml",false);
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        // 界面变量
        ResMenuCache.guiUrl = config.getString("gui.url");
        ResMenuCache.guiX = config.getInt("gui.x");
        ResMenuCache.guiY = config.getInt("gui.y");
        ResMenuCache.guiW = config.getInt("gui.w");
        ResMenuCache.guiH = config.getInt("gui.h");
        // 列表变量
        ResMenuCache.listX = config.getInt("list.x");
        ResMenuCache.listY = config.getInt("list.y");
        ResMenuCache.listW = config.getInt("list.w");
        ResMenuCache.listH = config.getInt("list.h");
        ResMenuCache.listStartX = config.getInt("list.startX");
        ResMenuCache.listStartY = config.getInt("list.startY");
        ResMenuCache.listIntervalX = config.getInt("list.intervalX");
        ResMenuCache.listIntervalY = config.getInt("list.intervalY");
        // 按钮变量
        ResMenuCache.buttonName = config.getString("button.name");
        ResMenuCache.buttonUrl1 = config.getString("button.url1");
        ResMenuCache.buttonUrl2 = config.getString("button.url2");
        ResMenuCache.buttonW = config.getInt("button.w");
        ResMenuCache.buttonH = config.getInt("button.h");
    }

    /**
     * 缓存ShortcutMenu配置文件
     * @param plugin 插件实例
     */
    private static void getCacheFromMenu(Plugin plugin){
        File file = new File(plugin.getDataFolder(),"ShortcutMenu.yml");
        if(!file.exists()){
            plugin.saveResource("ShortcutMenu.yml",false);
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        MenuCache.hotKey = config.getInt("option.hotKey");
        MenuCache.coolDown = config.getInt("option.coolDown");
        MenuCache.loosen = config.getBoolean("option.loosen");
        MenuCache.closable = config.getBoolean("option.closable");
        MenuCache.loading = config.getBoolean("option.loading");
        MenuCache.blackList = config.getStringList("option.blackList");
        ConfigurationSection menu = (ConfigurationSection) config.get("menu");
        ConcurrentHashMap<String,List<String>> map = new ConcurrentHashMap<>(16);
        menu.getKeys(false).forEach(key -> map.put(key,menu.getStringList(key)));
        MenuCache.menu = map;
    }

    /**
     * 缓存menu文件夹内的配置文件
     * @param plugin 插件实例
     */
    private static void getCacheFromMenuFolder(Plugin plugin){
        File folder = new File(plugin.getDataFolder(),"menu");
        if(!folder.exists()){
            plugin.saveResource("menu/default.yml",false);
        }
        ConcurrentHashMap<String,MenuCache> map = new ConcurrentHashMap<>(16);
        for(File file : Objects.requireNonNull(folder.listFiles())){
            if(file.getName().endsWith(".yml")){
                String name = file.getName().replace(".yml","");
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                String url = config.getString("gui.url");
                int x = config.getInt("gui.x");
                int y = config.getInt("gui.y");
                int w = config.getInt("gui.w");
                int h = config.getInt("gui.h");
                ConfigurationSection slot = (ConfigurationSection) config.get("slot");
                ConfigurationSection text = (ConfigurationSection) config.get("text");
                ConfigurationSection image = (ConfigurationSection) config.get("image");
                ConfigurationSection button = (ConfigurationSection) config.get("button");
                map.put(name,new MenuCache(url,x,y,w,h,getSlotCacheFromConfig(slot),getTextCacheFromConfig(text),getImageCacheFromConfig(image),getButtonCacheFromConfig(button)));
            }
        }
        MenuCache.data = map;
    }

    /**
     * 缓存slot节点的内容
     * @param config slot节点
     * @return 物品组件缓存
     */
    private static List<SlotCache> getSlotCacheFromConfig(ConfigurationSection config){
        List<SlotCache> list = new ArrayList<>();
        for(String key : config.getKeys(false)){
            int x = config.getInt(key+".x");
            int y = config.getInt(key+".y");
            ItemStack item = config.getItemStack(key+".item");
            list.add(new SlotCache(x,y,item));
        }
        return list;
    }

    /**
     * 缓存text节点的内容
     * @param config text节点
     * @return 文本组件缓存
     */
    private static List<TextCache> getTextCacheFromConfig(ConfigurationSection config){
        List<TextCache> list = new ArrayList<>();
        for(String key : config.getKeys(false)){
            int x = config.getInt(key+".x");
            int y = config.getInt(key+".y");
            double scale = config.getDouble(key+".scale");
            List<String> content = config.getStringList(key+".content");
            int hoverTextWidth = config.getInt(key+".hoverTextWidth");
            List<String> hoverText = config.getStringList(key+".hoverText");
            list.add(new TextCache(x,y,scale,content,hoverTextWidth,hoverText));
        }
        return list;
    }

    /**
     * 缓存image节点的内容
     * @param config image节点
     * @return 图片组件缓存
     */
    private static List<ImageCache> getImageCacheFromConfig(ConfigurationSection config){
        List<ImageCache> list = new ArrayList<>();
        for(String key : config.getKeys(false)){
            String url = config.getString(key+".url");
            int x = config.getInt(key+".x");
            int y = config.getInt(key+".y");
            int w = config.getInt(key+".w");
            int h = config.getInt(key+".h");
            boolean gif = config.getBoolean(key+".gif");
            int interval = config.getInt(key+".interval");
            int minU = config.getInt(key+".minU");
            int minV = config.getInt(key+".minV");
            int maxU = config.getInt(key+".maxU");
            int maxV = config.getInt(key+".maxV");
            List<String> hoverText = config.getStringList(key+".hoverText");
            list.add(new ImageCache(url,x,y,w,h,gif,interval,minU,minV,maxU,maxV,hoverText));
        }
        return list;
    }

    /**
     * 缓存button节点的内容
     * @param config button节点
     * @return 按钮组件缓存
     */
    private static ConcurrentHashMap<String,ButtonCache> getButtonCacheFromConfig(ConfigurationSection config){
        ConcurrentHashMap<String,ButtonCache> map = new ConcurrentHashMap<>(16);
        for(String key : config.getKeys(false)){
            String name = config.getString(key+".name");
            String url1 = config.getString(key+".url1");
            String url2 = config.getString(key+".url2");
            int w = config.getInt(key+".w");
            int h = config.getInt(key+".h");
            int x = config.getInt(key+".x");
            int y = config.getInt(key+".y");
            List<String> hoverText = config.getStringList(key+".hoverText");
            boolean close = config.getBoolean(key+".close");
            String webUrl = config.getString(key+".webUrl");
            int commandMode = config.getInt(key+".commandMode");
            LinkedHashMap<String,CommandCache> commandGroup = new LinkedHashMap<>();
            ConfigurationSection group = (ConfigurationSection) config.get(key+".commandGroup");
            for(String temp : group.getKeys(false)){
                List<String> script = group.getStringList(temp+".script");
                List<String> permission = group.getStringList(temp+".permission");
                List<String> commands = group.getStringList(temp+".commands");
                commandGroup.put(temp,new CommandCache(script,permission,commands));
            }
            map.put(key,new ButtonCache(name,url1,url2,x,y,w,h,hoverText,close,webUrl,commandMode,commandGroup));
        }
        return map;
    }
}
