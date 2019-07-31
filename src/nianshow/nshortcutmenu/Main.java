package nianshow.nshortcutmenu;

import me.clip.placeholderapi.PlaceholderAPI;
import nianshow.nshortcutmenu.cache.MenuCache;
import nianshow.nshortcutmenu.cache.PlayerCache;
import nianshow.nshortcutmenu.listener.PlayerListener;
import nianshow.nshortcutmenu.listener.VexListener;
import nianshow.nshortcutmenu.papi.ShortcutMenu;
import nianshow.nshortcutmenu.utils.BaseUtils;
import nianshow.nshortcutmenu.utils.FileUtils;
import nianshow.nshortcutmenu.utils.ResUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Nianshow
 */
public class Main extends JavaPlugin{
    public static Plugin plugin;
    private static boolean residence;
    private String[] subCommands = {"reload","set","open","close","item","res"};

    @Override
    public void onEnable(){
        // 初始化变量
        plugin = this;
        residence = Bukkit.getPluginManager().isPluginEnabled("Residence");
        // 输出自己的大名
        Bukkit.getConsoleSender().sendMessage("§6NShortcutMenu§r: {");
        Bukkit.getConsoleSender().sendMessage("  作者: §e粘兽§r,");
        Bukkit.getConsoleSender().sendMessage("  版本: §e"+getDescription().getVersion()+"§r,");
        boolean depend = Bukkit.getPluginManager().isPluginEnabled("VexView") && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
        // 判断必要附属是否已加载
        if(depend){
            Bukkit.getConsoleSender().sendMessage("  状态: §2true");
            Bukkit.getConsoleSender().sendMessage("}");
            // 注册事件监听器
            Bukkit.getPluginManager().registerEvents(new PlayerListener(),this);
            Bukkit.getPluginManager().registerEvents(new VexListener(),this);
            // 注册怕屁变量
            PlaceholderAPI.registerPlaceholderHook(getName(),new ShortcutMenu());
            // 载入配置文件
            FileUtils.reloadCache(this);
        }else{
            Bukkit.getConsoleSender().sendMessage("  状态: §4false§r,");
            Bukkit.getConsoleSender().sendMessage("  原因: §c插件需要§bVexView与PlaceholderAPI§c作为前置§r");
            Bukkit.getConsoleSender().sendMessage("}");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender,Command command,String label,String[] args){
        if(!sender.isOp()){return false;}
        int num = 0;
        if(args.length > num){
            switch(args[0]){
                case "reload":
                    FileUtils.reloadCache(this);
                    sender.sendMessage("§a> 重置配置文件完毕");
                    return true;
                case "set":
                    num = 2;
                    if(args.length > num){
                        return cmdSetPage(sender,args);
                    }
                    break;
                case "open":
                    num = 2;
                    if(args.length > num){
                        return cmdOpenMenu(sender,args);
                    }
                    break;
                case "close":
                    num = 1;
                    if(args.length > num){
                        return cmdCloseMenu(sender,args);
                    }
                    break;
                case "item":
                    num = 2;
                    if(args.length > num){
                        return cmdSlotItem(sender,args);
                    }
                case "res":
                    num = 1;
                    if(args.length > num){
                        return cmdResMenu(sender,args);
                    }
                default:
            }
        }
        // 命令提示
        sender.sendMessage("§m------------------------------------------------------------");
        sender.sendMessage("§7> /nsm reload - 重载配置文件");
        sender.sendMessage("§7> /nsm set <玩家> <页码> - 设置页码");
        sender.sendMessage("§7> /nsm open <玩家> <菜单组> - 打开菜单组");
        sender.sendMessage("§7> /nsm close <玩家> - 关闭当前界面");
        sender.sendMessage("§7> /nsm item <菜单> <槽位> - 将手中物品设为该槽位所展示物品");
        sender.sendMessage("§m------------------------------------------------------------");
        sender.sendMessage("§7> /nsm res <玩家> - 打开领地菜单");
        sender.sendMessage("§m------------------------------------------------------------");
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command command,String alias,String[] args){
        if(!sender.isOp()){return new ArrayList<>();}
        int num = 2;
        switch(args[0]){
            case "reload":
                return new ArrayList<>();
            case "set":
                if(args.length == num){
                    return null;
                }
                num = 3;
                if(args.length == num){
                    sender.sendMessage("§a> 请输入一个整数");
                    return new ArrayList<>();
                }
                return new ArrayList<>();
            case "open":
                if(args.length == num){
                    return null;
                }
                num = 3;
                if(args.length == num){
                    int temp = num - 1;
                    return Arrays.stream(MenuCache.menu.keySet().toArray(new String[]{})).filter(s -> s.startsWith(args[temp])).collect(Collectors.toList());
                }
                return new ArrayList<>();
            case "close":
                return null;
            case "item":
                if(args.length == num){
                    int temp = num - 1;
                    return Arrays.stream(MenuCache.data.keySet().toArray(new String[]{})).filter(s -> s.startsWith(args[temp])).collect(Collectors.toList());
                }
                num = 3;
                if(args.length == num){
                    int temp = num - 1;
                    File file = new File(getDataFolder(),"menu/"+args[1]+".yml");
                    YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
                    ConfigurationSection slot = (ConfigurationSection) yaml.get("slot");
                    return Arrays.stream(slot.getKeys(false).toArray(new String[]{})).filter(s -> s.startsWith(args[temp])).collect(Collectors.toList());
                }
            case "res":
                return null;
            default:
        }
        return Arrays.stream(subCommands).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
    }

    /**
     * 检查玩家是否存在
     * @param sender 命令执行者
     * @param player 玩家对象
     * @return 是否存在
     */
    private boolean checkPlayer(CommandSender sender,Player player){
        if(player == null || !player.isOnline()){
            sender.sendMessage("§c> 玩家不存在");
            return false;
        }
        return true;
    }

    /**
     * 设置页码
     * @param sender 命令执行者
     * @param args 命令参数数组
     * @return 是否成功执行
     */
    private boolean cmdSetPage(CommandSender sender,String[] args){
        Player player = Bukkit.getPlayer(args[1]);
        if(checkPlayer(sender,player)){
            try{
                if(!PlayerCache.cache.containsKey(player.getUniqueId())){
                    BaseUtils.createCache(player);
                }
                int page = Integer.parseInt(args[2]);
                page = page < 1 ? 1 : page;
                PlayerCache.cache.get(player.getUniqueId()).setPage(page - 1);
                sender.sendMessage("§a> 将玩家§f"+player.getName()+"§a的页码设置为§f"+page);
                return true;
            }catch(Exception e){
                sender.sendMessage("§c> §f"+args[2]+"§c不是一个整数");
            }
        }
        return false;
    }

    /**
     * 打开菜单组
     * @param sender 命令执行者
     * @param args 命令参数数组
     * @return 是否成功执行
     */
    private boolean cmdOpenMenu(CommandSender sender,String[] args){
        Player player = Bukkit.getPlayer(args[1]);
        if(checkPlayer(sender,player)){
            if(!PlayerCache.cache.containsKey(player.getUniqueId())){
                BaseUtils.createCache(player);
            }
            int page = PlayerCache.cache.get(player.getUniqueId()).getPage();
            List<String> menuList = MenuCache.menu.get(MenuCache.menu.containsKey(args[2]) ? args[2] : "default");
            if(page > menuList.size()){
                page = menuList.size() - 1;
                PlayerCache.cache.get(player.getUniqueId()).setPage(page);
            }
            String menu = menuList.get(page);
            BaseUtils.openShortcutMenu(player,MenuCache.data.get(menu));
            sender.sendMessage("§a> 给玩家§f"+player.getName()+"§a打开菜单组§f"+menu);
            return true;
        }
        return false;
    }

    /**
     * 关闭当前界面
     * @param sender 命令执行者
     * @param args 命令参数数组
     * @return 是否成功执行
     */
    private boolean cmdCloseMenu(CommandSender sender,String[] args){
        Player player = Bukkit.getPlayer(args[1]);
        if(checkPlayer(sender,player)){
            player.closeInventory();
            PlayerCache.cache.get(player.getUniqueId()).setMenu("");
            sender.sendMessage("§a> 关闭玩家§f"+player.getName()+"§a当前界面");
            return true;
        }
        return false;
    }

    /**
     * 设置菜单槽位物品
     * @param sender 命令执行者
     * @param args 命令参数数组
     * @return 是否成功执行
     */
    private boolean cmdSlotItem(CommandSender sender,String[] args){
        if(sender instanceof Player){
            if(!MenuCache.data.containsKey(args[1])){
                sender.sendMessage("§c> 菜单§f"+args[1]+"§c不存在");
                return false;
            }
            Bukkit.getScheduler().runTaskAsynchronously(this,() -> {
                try{
                    ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
                    File file = new File(getDataFolder(),"menu/"+args[1]+".yml");
                    YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
                    yaml.set("slot."+args[2]+".item",item);
                    yaml.save(file);
                    sender.sendMessage("§a> 成功将菜单§f"+args[1]+"§a的槽位组件§f"+args[2]+"§a的物品设置为手中物品");
                    FileUtils.reloadCache(this);
                }catch(Exception e){
                    sender.sendMessage("[NShortcutMenu] §c保存菜单§f"+args[1]+"§c的槽位组件§f"+args[2]+"§c的物品时发生错误!");
                    e.printStackTrace();
                }
            });
            return true;
        }
        sender.sendMessage("§c> 该命令只能由玩家使用");
        return false;
    }

    /**
     * 打开领地菜单
     * @param sender 命令执行者
     * @param args 命令参数数组
     * @return 是否成功执行
     */
    private boolean cmdResMenu(CommandSender sender,String[] args){
        if(!Main.residence){
            sender.sendMessage("§c> 本功能需要§fResidence§c作为前置");
            return false;
        }
        Player player = Bukkit.getPlayer(args[1]);
        if(checkPlayer(sender,player)){
            ResUtils.openResMenu(player);
        }
        return false;
    }
}
