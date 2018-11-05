package stev.echanson.classes;

import java.util.ArrayList;
import java.util.List;

public class Nourriture {
    private double energie = 0;
    private double gras = 0;
    private double sucre = 0;
    private double proteine = 0;
    private double sel = 0;
    private List<String> warnings;

    public Nourriture(){
        warnings=new ArrayList<>();
    }

    public Nourriture(double energie, double gras, double sucre, double proteine, double sel) {
        this.energie = energie;
        this.gras = gras;
        this.sucre = sucre;
        this.proteine = proteine;
        this.sel = sel;
        warnings=new ArrayList<>();
    }

    public Nourriture(double energie, double gras, double sucre, double proteine, double sel, List<String> warnings) {
        this.energie = energie;
        this.gras = gras;
        this.sucre = sucre;
        this.proteine = proteine;
        this.sel = sel;
        this.warnings=warnings;
    }

    public double getEnergie() {
        return energie;
    }

    public double getGras() {
        return gras;
    }

    public double getSucre() {
        return sucre;
    }

    public double getProteine() {
        return proteine;
    }

    public double getSel() {
        return sel;
    }

    public List<String> getWarnings() {
        return warnings;
    }
}
