package stev.echanson.classes;

import java.util.List;

public class Categorie {
    private String name;
    private List<String> relatedCategories;

    public Categorie(String name, List<String> relatedCategories){
        this.name = name;
        this.relatedCategories = relatedCategories;
    }

    public String getName() {
        return name;
    }
}
