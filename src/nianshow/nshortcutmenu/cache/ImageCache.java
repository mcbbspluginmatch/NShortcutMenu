package nianshow.nshortcutmenu.cache;

import java.util.List;

/**
 * @author Nianshow
 */
public class ImageCache{
    public String url;
    public int x;
    public int y;
    public int w;
    public int h;
    public boolean gif;
    public int interval;
    public int minU;
    public int minV;
    public int maxU;
    public int maxV;
    public List<String> hoverText;

    public ImageCache(String url,int x,int y,int w,int h,boolean gif,int interval,int minU,int minV,int maxU,int maxV,List<String> hoverText){
        this.url = url;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.gif = gif;
        this.interval = interval;
        this.minU = minU;
        this.minV = minV;
        this.maxU = maxU;
        this.maxV = maxV;
        this.hoverText = hoverText;
    }
}
