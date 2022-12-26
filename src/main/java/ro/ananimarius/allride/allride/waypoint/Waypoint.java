package ro.ananimarius.allride.allride.waypoint;

import javax.persistence.*;

@Entity
@Table
public class Waypoint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //we use auto increment because it's convenient and simple
    private Long id;
    private long time;
    private double latitude; //we use simple primitives to store the location instead of using a special object
    //mysql have special datatype for working with GIS (geographic information system), but i chose not to use them because
    //my system is simple by comparison. The mapping of those datatypes into hibernate is done through a very complex API known as JTS
    private double longitute;
    private float direction;

    //<editor-fold desc="Getters & setters">
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitute() {
        return longitute;
    }

    public void setLongitute(double longitute) {
        this.longitute = longitute;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }
    //</editor-fold>


    public Waypoint(Long id, long time, double latitude, double longitute, float direction) {
        this.id = id;
        this.time = time;
        this.latitude = latitude;
        this.longitute = longitute;
        this.direction = direction;
    }
    public Waypoint(){}
}
