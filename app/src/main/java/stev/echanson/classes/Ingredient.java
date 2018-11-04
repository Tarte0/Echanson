package stev.echanson.classes;

import java.util.List;

public class Ingredient {
    private String name;
    private List<String> categories;

    public Ingredient(String name,List<String> categories){
        this.name = name;
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public List<String> getCategories() { return categories; }
}

