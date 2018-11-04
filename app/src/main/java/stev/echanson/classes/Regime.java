package stev.echanson.classes;

import java.util.List;

public class Regime {
    private String name;
    private String icon;
    private List<String> forbids;

    public Regime(String name, String icon, List<String> forbids){
        this.name = name;
        this.icon = icon;
        this.forbids = forbids;
    }

    public String getName() {
        return name;
    }

    public String getIcon(){
        return icon;
    }

    public List<String> getForbids() { return forbids; }
}

