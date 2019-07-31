package nianshow.nshortcutmenu.cache;

import java.util.List;

/**
 * @author Nianshow
 */
public class CommandCache{
    public List<String> script;
    public List<String> permission;
    public List<String> commands;

    public CommandCache(List<String> script,List<String> permission,List<String> commands){
        this.script = script;
        this.permission = permission;
        this.commands = commands;
    }
}
