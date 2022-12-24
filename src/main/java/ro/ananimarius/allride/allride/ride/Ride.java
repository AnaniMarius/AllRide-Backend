package ro.ananimarius.allride.allride.ride;

import ro.ananimarius.allride.allride.user.User;
import ro.ananimarius.allride.allride.waypoint.Waypoint;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User passenger; //the passenger and driver are implicitly stored as relations within the database by JPA

    @ManyToOne
    private User driver;

    @OneToMany //same for the waypoint set which wil include our route as a set of points on the map with a time stamp for each point
    @OrderBy("time ASC") //jpa sorts the waypoint set which will include our route as a set of points on the map with a time stamp for each point
    private Set<Waypoint> route;
    private BigDecimal cost;
    private String currency;
}
