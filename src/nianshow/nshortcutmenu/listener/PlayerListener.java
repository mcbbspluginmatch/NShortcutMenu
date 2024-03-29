package nianshow.nshortcutmenu.listener;

import lk.vexview.VexView;
import lk.vexview.event.PlayerClientWindowSizeEvent;
import lk.vexview.event.VerificationFinishEvent;
import nianshow.nshortcutmenu.Main;
import nianshow.nshortcutmenu.cache.PlayerCache;
import nianshow.nshortcutmenu.utils.BaseUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

/**
 * @author Nianshow
 */
public class PlayerListener implements Listener{
    @EventHandler
    public void onVerFinish(VerificationFinishEvent e){
        // VexView初始化完成 添加缓存
        Bukkit.getScheduler().runTask(Main.plugin,() -> BaseUtils.createCache(e.getPlayer()));
    }

    @EventHandler
    public void onChange(PlayerClientWindowSizeEvent e){
        // 该玩家是否有缓存 没有则新建
        UUID uuid = e.getPlayer().getUniqueId();
        if(PlayerCache.cache.containsKey(uuid)){
            PlayerCache cache = PlayerCache.cache.get(uuid);
            int width = VexView.getInstance().getPlayerClientWindowWidth(e.getPlayer());
            int height = VexView.getInstance().getPlayerClientWindowHeight(e.getPlayer());
            // 对比新旧尺寸
            if(!cache.equals(width,height)){
                cache.setWidth(width);
                cache.setHeight(height);
            }
        }else{
            BaseUtils.createCache(e.getPlayer());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        // 退服清理缓存
        PlayerCache.cache.remove(e.getPlayer().getUniqueId());
    }
}
