package nianshow.nshortcutmenu.cache;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Nianshow
 */
public class MenuCache{
    public static int hotKey;
    public static int coolDown;
    public static boolean loosen;
    public static boolean closable;
    public static boolean loading;
    public static List<String> blackList;
    public static ConcurrentHashMap<String,List<String>> menu;

    public static ConcurrentHashMap<String,MenuCache> data;
    public String url;
    public int x;
    public int y;
    public int w;
    public int h;
    public List<SlotCache> slot;
    public List<TextCache> text;
    public List<ImageCache> image;
    public ConcurrentHashMap<String,ButtonCache> button;

    public MenuCache(String url,int x,int y,int w,int h,List<SlotCache> slot,List<TextCache> text,List<ImageCache> image,ConcurrentHashMap<String,ButtonCache> button){
        this.url = url;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.slot = slot;
        this.text = text;
        this.image = image;
        this.button = button;
    }
}
