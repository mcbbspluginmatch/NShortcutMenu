package nianshow.nshortcutmenu.event;

import lk.vexview.gui.VexGui;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author Nianshow
 */
public class ShortcutMenuOpen extends Event{
    private static HandlerList HANDLER_LIST = new HandlerList();
    private Player player;
    private VexGui gui;
    private int slotID;

    public ShortcutMenuOpen(Player player,VexGui gui,int slotID){
        this.player = player;
        this.gui = gui;
        this.slotID = slotID;
    }

    @Override
    public HandlerList getHandlers(){
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList(){
        return HANDLER_LIST;
    }

    public VexGui getGui(){
        return gui;
    }

    public Player getPlayer(){
        return player;
    }

    public int getSlotID(){
        return ++slotID;
    }
}
