package stev.echanson.classes;

public class Food {
    String date;
    String nourriture;
    String picture;

    public Food(String date, String nourriture, String picture){
        this.date = date;
        this. nourriture = nourriture;
        this.picture = picture;
    }

    public String getDate(){
        return date;
    }

    public String getNourriture() {
        return nourriture;
    }

    public String getPicture() {
        return picture;
    }
}
