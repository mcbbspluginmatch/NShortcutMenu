package nianshow.nshortcutmenu.cache;

import org.bukkit.inventory.ItemStack;

/**
 * @author Nianshow
 */
public class SlotCache{
    public int x;
    public int y;
    public ItemStack item;

    public SlotCache(int x,int y,ItemStack item){
        this.x = x;
        this.y = y;
        this.item = item;
    }
}
