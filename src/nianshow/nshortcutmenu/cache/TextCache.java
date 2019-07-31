package nianshow.nshortcutmenu.cache;

import java.util.List;

/**
 * @author Nianshow
 */
public class TextCache{
    public int x;
    public int y;
    public double scale;
    public List<String> content;
    public int hoverTextWidth;
    public List<String> hoverText;

    public TextCache(int x,int y,double scale,List<String> content,int hoverTextWidth,List<String> hoverText){
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.content = content;
        this.hoverTextWidth = hoverTextWidth;
        this.hoverText = hoverText;
    }
}
