package mx.itesm.wkt.gotita;

import com.google.firebase.firestore.GeoPoint;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Offer extends OfferId{
    private boolean active;
    private String description;
    private ArrayList<String> images;
    private HashMap<String,Double> price;
    private String title;
    private String type;
    private String user;
//    private HashMap<String,Double> location;
    private GeoPoint location;
    private int range;
    private HashMap<String,String> schedule;


    public Offer(){}

    public Offer(boolean active, String description, ArrayList<String> images, HashMap<String,Double> price, String title, String type, String user, GeoPoint location,int range, HashMap<String,String> schedule){
        this.active=active;
        this.description=description;
        this.images=images;
        this.price=price;
        this.title=title;
        this.type=type;
        this.user=user;
        this.location=location;
        this.range = range;
        this.schedule = schedule;
    }

    public HashMap<String, String> getSchedule() {
        return schedule;
    }

    public int getRange(){
        return range;
    }

    public boolean isActive() {
        return active;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public HashMap<String,Double> getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getUser() {
        return user;
    }

    public GeoPoint getLocation() {
        return location;
    }
}
