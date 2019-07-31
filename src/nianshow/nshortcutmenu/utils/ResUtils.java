package nianshow.nshortcutmenu.utils;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.VexGui;
import lk.vexview.gui.components.ScrollingListComponent;
import lk.vexview.gui.components.VexButton;
import lk.vexview.gui.components.VexScrollingList;
import nianshow.nshortcutmenu.cache.MenuCache;
import nianshow.nshortcutmenu.cache.ResMenuCache;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nianshow
 */
public class ResUtils{
    public static void openResMenu(Player player){
        VexGui gui = new VexGui(ResMenuCache.guiUrl,ResMenuCache.guiX,ResMenuCache.guiY,ResMenuCache.guiW,ResMenuCache.guiH,ResMenuCache.guiW,ResMenuCache.guiH);
        gui.isLoading(MenuCache.loading);
        List<VexButton> buttonList = new ArrayList<>();
        // 遍历该玩家的领地列表
        List<String> list = Residence.getInstance().getPlayerManager().getResidenceList(player.getUniqueId());
        for(int i = 0; i < list.size(); i++){
            ClaimedResidence residence = Residence.getInstance().getResidenceManager().getByName(list.get(i));
            Location loc = residence.getTeleportLocation(player);
            String id = "ResidenceMenu:"+i;
            String name = ResMenuCache.buttonName.replace("{name}",list.get(i)).
                    replace("{x}",Integer.toString(loc.getBlockX())).
                    replace("{y}",Integer.toString(loc.getBlockY())).
                    replace("{z}",Integer.toString(loc.getBlockZ()));
            int x = ResMenuCache.listStartX + ResMenuCache.listIntervalX * i;
            int y = ResMenuCache.listStartY + ResMenuCache.listIntervalY * i;
            buttonList.add(new VexButton(id,name,ResMenuCache.buttonUrl1,ResMenuCache.buttonUrl2,x,y,ResMenuCache.buttonW,ResMenuCache.buttonH));
        }
        // 安排滚动条组件
        int full = (list.size() - 1) * (ResMenuCache.listIntervalY + ResMenuCache.buttonH);
        VexScrollingList scrollingList = new VexScrollingList(ResMenuCache.listX,ResMenuCache.listY,ResMenuCache.listW,ResMenuCache.listH,full);
        buttonList.forEach(scrollingList::addComponent);
        gui.addComponent(scrollingList);
        VexViewAPI.openGui(player,gui);
    }
}
