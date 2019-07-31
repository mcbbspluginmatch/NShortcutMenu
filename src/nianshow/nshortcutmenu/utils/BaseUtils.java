package nianshow.nshortcutmenu.utils;

import lk.vexview.VexView;
import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.VexGui;
import me.clip.placeholderapi.PlaceholderAPI;
import nianshow.nshortcutmenu.Main;
import nianshow.nshortcutmenu.cache.CommandCache;
import nianshow.nshortcutmenu.cache.MenuCache;
import nianshow.nshortcutmenu.cache.PlayerCache;
import nianshow.nshortcutmenu.event.ShortcutMenuOpen;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * @author Nianshow
 */
public class BaseUtils{
    private static ScriptEngine javaScript = new ScriptEngineManager().getEngineByName("JavaScript");

    /**
     * 创建玩家缓存
     * @param player 玩家
     */
    public static void createCache(Player player){
        int page = 0;
        int width = VexView.getInstance().getPlayerClientWindowWidth(player);
        int height = VexView.getInstance().getPlayerClientWindowHeight(player);
        PlayerCache.cache.put(player.getUniqueId(),new PlayerCache(page,width,height,""));
    }

    /**
     * 执行命令
     * @param player 玩家
     * @param cache 命令组缓存
     * @return 是否成功执行
     */
    public static boolean runCommands(Player player,CommandCache cache){
        // 权限条件判断
        for(String permission : cache.permission){
            if(!player.hasPermission(permission)){
                return false;
            }
        }
        // JavaScript 条件判断
        for(String script : PlaceholderAPI.setPlaceholders(player,cache.script)){
            try{
                if(!(boolean) javaScript.eval(script)){
                    return false;
                }
            }catch(Exception e){
                Bukkit.getConsoleSender().sendMessage("[NShortcutMenu] §c"+player.getName()+"在判断按钮命令条件时发生错误!");
                Bukkit.getConsoleSender().sendMessage("§c条件内容: §f"+script);
                e.printStackTrace();
                return false;
            }
        }
        // 通过条件 运行命令
        for(String cmd : PlaceholderAPI.setPlaceholders(player,cache.commands)){
            try{
                // 使用管理员身份执行命令
                if(cmd.startsWith("op:")){
                    if(player.isOp()){
                        Bukkit.dispatchCommand(player,cmd.replace("op:",""));
                    }else{
                        player.setOp(true);
                        Bukkit.dispatchCommand(player,cmd.replace("op:",""));
                        player.setOp(false);
                    }
                    continue;
                }
                // 使用后台身份执行命令
                if(cmd.startsWith("console:")){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),cmd.replace("console:",""));
                    continue;
                }
                // 正常执行命令
                Bukkit.dispatchCommand(player,cmd);
            }catch(Exception e){
                // 取消管理员身份并打印消息
                player.setOp(false);
                Bukkit.getConsoleSender().sendMessage("[NShortcutMenu] §c"+player.getName()+"在执行按钮命令时发生错误!");
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 打开菜单
     * @param player 玩家
     * @param cache 菜单缓存
     */
    public static void openShortcutMenu(Player player,MenuCache cache){
        VexGui gui = new VexGui(cache.url,cache.x,cache.y,cache.w,cache.h,cache.w,cache.h);
        gui.isLoading(MenuCache.loading);
        gui.setClosable(MenuCache.closable);
        // 添加组件
        int slotID = 0;
        for(;slotID < cache.slot.size();slotID++){
            gui.addComponent(VexUtils.createSlot(slotID,cache.slot.get(slotID)));
        }
        cache.image.forEach(key -> gui.addComponent(VexUtils.createImage(key)));
        cache.text.forEach(key -> gui.addComponent(VexUtils.createText(key)));
        cache.button.forEach((key,value) -> gui.addComponent(VexUtils.createButton(key,value)));
        // 触发事件
        Bukkit.getPluginManager().callEvent(new ShortcutMenuOpen(player,gui,slotID));
        Bukkit.getScheduler().runTask(Main.plugin,() -> VexViewAPI.openGui(player,gui));
    }
}
