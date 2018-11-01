package stev.echanson.classes;

import java.util.List;

public class Regime {
    private String name;
    private List<String> relatedCategories;

    public Regime(String name, List<String> relatedCategories){
        this.name = name;
        this.relatedCategories = relatedCategories;
    }

    public String getName() {
        return name;
    }
}

