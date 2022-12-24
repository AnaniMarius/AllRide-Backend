package ro.ananimarius.allride.allride.rating;

import ro.ananimarius.allride.allride.user.User;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

public class Rating {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private long date;
    private float score;
    private String comment;

    @ManyToOne
    private User passenger;

    @ManyToOne
    private User driver;

    //<editor-fold desc="Getters & setters">
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getPassenger() {
        return passenger;
    }

    public void setPassenger(User passenger) {
        this.passenger = passenger;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }
    //</editor-fold>


    public Rating(Long id, long date, float score, String comment, User passenger, User driver) {
        this.id = id;
        this.date = date;
        this.score = score;
        this.comment = comment;
        this.passenger = passenger;
        this.driver = driver;
    }
}
