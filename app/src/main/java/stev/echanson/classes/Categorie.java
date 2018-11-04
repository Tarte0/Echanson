package stev.echanson.classes;

import java.util.List;

public class Categorie {
    private String name;
    private List<String> children;
    private List<String> parent;
    private List<String> relatedRegime;

    public Categorie(String name, List<String> children, List<String> parent, List<String> relatedRegime){
        this.name = name;
        this.children = children;
        this.parent = parent;
        this.relatedRegime = relatedRegime;
    }

    public String getName() {
        return name;
    }

    public List<String> getChildren() { return children; }

    public List<String> getParent() { return parent; }

    public List<String> getRelatedRegime() { return relatedRegime; }
}
