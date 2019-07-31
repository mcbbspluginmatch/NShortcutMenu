package nianshow.nshortcutmenu.papi;

import me.clip.placeholderapi.PlaceholderHook;
import nianshow.nshortcutmenu.cache.MenuCache;
import nianshow.nshortcutmenu.cache.PlayerCache;
import nianshow.nshortcutmenu.utils.VexUtils;
import org.bukkit.entity.Player;

/**
 * @author Nianshow
 */
public class ShortcutMenu extends PlaceholderHook{
    @Override
    public String onPlaceholderRequest(Player player,String string){
        if(player != null){
            switch(string){
                case "page":
                    return Integer.toString(PlayerCache.cache.get(player.getUniqueId()).getPage() + 1);
                case "hotKey":
                    return VexUtils.getKeyName(MenuCache.hotKey);
                default:
            }
        }
        return null;
    }
}
