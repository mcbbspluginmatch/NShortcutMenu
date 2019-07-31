package nianshow.nshortcutmenu.listener;

import lk.vexview.event.ButtonClickEvent;
import lk.vexview.event.KeyBoardPressEvent;
import nianshow.nshortcutmenu.Main;
import nianshow.nshortcutmenu.cache.ButtonCache;
import nianshow.nshortcutmenu.cache.MenuCache;
import nianshow.nshortcutmenu.cache.PlayerCache;
import nianshow.nshortcutmenu.utils.BaseUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Nianshow
 */
public class VexListener implements Listener{
    private List<String> passList = new ArrayList<>();

    @EventHandler
    public void onPress(KeyBoardPressEvent e){
        if(e.getKey() == MenuCache.hotKey){
            UUID uuid = e.getPlayer().getUniqueId();
            // 判断玩家是否拥有缓存
            if(!PlayerCache.cache.containsKey(uuid)){
                BaseUtils.createCache(e.getPlayer());
            }
            String type = "NOGUI";
            if(e.getEventKeyState() && e.getType().name().equalsIgnoreCase(type)){
                // 冷却判断
                if(passList.contains(e.getPlayer().getName())){return;}
                passList.add(e.getPlayer().getName());
                Bukkit.getScheduler().runTaskLater(Main.plugin,() -> passList.remove(e.getPlayer().getName()),MenuCache.coolDown);
                // 黑名单判断
                String world = e.getPlayer().getWorld().getName();
                for(String key : MenuCache.blackList){
                    if(key.equalsIgnoreCase(world)){
                        return;
                    }
                }
                // 根据页码和世界名打开菜单
                int page = PlayerCache.cache.get(uuid).getPage();
                List<String> menuList = MenuCache.menu.get(MenuCache.menu.containsKey(world) ? world : "default");
                // 防止越界
                if(page > menuList.size()){
                    page = menuList.size() - 1;
                    PlayerCache.cache.get(uuid).setPage(page);
                }
                String menu = menuList.get(page);
                BaseUtils.openShortcutMenu(e.getPlayer(),MenuCache.data.get(menu));
                PlayerCache.cache.get(uuid).setMenu(menu);
            }else if(!e.getEventKeyState() && MenuCache.loosen){
                e.getPlayer().closeInventory();
                PlayerCache.cache.get(uuid).setMenu("");
            }
        }
    }

    @EventHandler
    public void onClick(ButtonClickEvent e){
        UUID uuid = e.getPlayer().getUniqueId();
        // 判断是否为领地传送按钮
        if(e.getButtonID() instanceof String){
            boolean isRes = ((String) e.getButtonID()).startsWith("ResidenceMenu:");
            if(isRes){
                int num = Integer.parseInt(((String) e.getButtonID()).replace("ResidenceMenu:",""));
                List<String> list = com.bekvon.bukkit.residence.Residence.getInstance().getPlayerManager().getResidenceList(uuid);
                Bukkit.dispatchCommand(e.getPlayer(),"res tp "+list.get(num));
                return;
            }
        }
        // 判断是否有缓存且打开了菜单
        if(PlayerCache.cache.containsKey(uuid) && PlayerCache.cache.get(uuid).hasMenu()){
            String menu = PlayerCache.cache.get(uuid).getMenu();
            ButtonCache cache = MenuCache.data.get(menu).button.get(e.getButtonID().toString());
            // 遍历命令组
            for(String key : cache.commandGroup.keySet()){
                // 模式0 一直循环一直爽
                // 模式1 返回真跳出循环
                // 模式2 返回假跳出循环
                switch(cache.commandMode){
                    case 0:
                        BaseUtils.runCommands(e.getPlayer(),cache.commandGroup.get(key));
                        continue;
                    case 1:
                        if(BaseUtils.runCommands(e.getPlayer(),cache.commandGroup.get(key))){
                            return;
                        }
                        continue;
                    case 2:
                        if(!BaseUtils.runCommands(e.getPlayer(),cache.commandGroup.get(key))){
                            return;
                        }
                        continue;
                    default:
                }
            }
        }
    }
}
