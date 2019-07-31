package nianshow.nshortcutmenu.cache;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Nianshow
 */
public class ButtonCache{
    public String name;
    public String url1;
    public String url2;
    public int x;
    public int y;
    public int w;
    public int h;
    public List<String> hoverText;
    public boolean close;
    public String webUrl;
    public int commandMode;
    public LinkedHashMap<String,CommandCache> commandGroup;

    public ButtonCache(String name,String url1,String url2,int x,int y,int w,int h,List<String> hoverText,boolean close,String webUrl,int commandMode,LinkedHashMap<String,CommandCache> commandGroup){
        this.name = name;
        this.url1 = url1;
        this.url2 = url2;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.hoverText = hoverText;
        this.close = close;
        this.webUrl = webUrl;
        this.commandMode = commandMode;
        this.commandGroup = commandGroup;
    }
}
