package stev.echanson.classes;

public class NutrimentsUnits {
    private String energie;
    private String gras;
    private String sucre;
    private String proteine;
    private String sel;

    public NutrimentsUnits() {
        this.energie = "kcal";
        this.gras = "g";
        this.sucre = "g";
        this.proteine = "g";
        this.sel = "g";
    }

    public NutrimentsUnits(String energie, String gras, String sucre, String proteine, String sel) {
        this.energie = energie;
        this.gras = gras;
        this.sucre = sucre;
        this.proteine = proteine;
        this.sel = sel;
    }

    public String getEnergie() {
        return energie;
    }

    public String getGras() {
        return gras;
    }

    public String getSucre() {
        return sucre;
    }

    public String getProteine() {
        return proteine;
    }

    public String getSel() {
        return sel;
    }
}
