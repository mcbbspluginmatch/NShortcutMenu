package nianshow.nshortcutmenu.cache;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Nianshow
 */
public class PlayerCache{
    public static ConcurrentHashMap<UUID,PlayerCache> cache = new ConcurrentHashMap<>();
    private int page;
    private int width;
    private int height;
    private String menu;

    public PlayerCache(int page,int width,int height,String menu){
        this.page = page;
        this.width = width;
        this.height = height;
        this.menu = menu;
    }

    public int getPage(){
        return page;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public String getMenu(){
        return menu;
    }

    public void setPage(int page){
        this.page = page;
    }

    public void setWidth(int width){
        this.width = width;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public void setMenu(String menu){
        this.menu = menu;
    }

    public boolean equals(int width,int height){
        return this.width == width && this.height == height;
    }

    public boolean hasMenu(){
        return menu != null && !menu.isEmpty();
    }
}
