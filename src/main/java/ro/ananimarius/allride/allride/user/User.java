package ro.ananimarius.allride.allride.user;

import org.springframework.data.geo.Point;
import ro.ananimarius.allride.allride.UserDAO.UserDAO;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table
public class User {
    //the user is used for both drivers and customers, even tho is not so much "polymerphic", but it's simpler

    //<editor-fold desc="Authorization">
    //authorization
    @Column(unique = true)
    private String authToken; //use authorization keys to block hackers that reverse engineer the
                              //client side code or sniff the network traffic
    public User(){
        authToken = UUID.randomUUID().toString(); //uuid class generates a completely random 16 character
        //long unique string (universal unique identifier)
    }
    //</editor-fold>

    //<editor-fold desc="Attributes">
    //unique identifier
    @Id
    //
//    @SequenceGenerator(
//            name="user_sequence",
//            sequenceName="user_sequence",
//            allocationSize = 1
//    )
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE,generator ="user_sequence")
    private long id;
    String firstName;
    String lastName;
    @Lob
    byte[] avatar;
    String phone;
    String email;
    String password;//the password will be stored as a salted hash of the password
    String facebookId;
    String googleId;
    boolean driver;
    String car;
    Point location;
    boolean hailing;
    Long assignedUser;
    float currentRating;
    //the direction the user is headed to
    double latitude;
    double longitute;
    float direction;


    //</editor-fold>

    //<editor-fold desc="Getters">
    public String getAuthToken() {
        return authToken;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public String getGoogleId() {
        return googleId;
    }

    public boolean isDriver() {
        return driver;
    }

    public String getCar() {
        return car;
    }

    public Point getLocation() {
        return location;
    }

    public boolean isHailing() {
        return hailing;
    }

    public Long getAssignedUser() {
        return assignedUser;
    }

    public float getCurrentRating() {
        return currentRating;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitute() {
        return longitute;
    }

    public float getDirection() {
        return direction;
    }
    //</editor-fold>

    //<editor-fold desc="Setters">
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public void setDriver(boolean driver) {
        this.driver = driver;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public void setHailing(boolean hailing) {
        this.hailing = hailing;
    }

    public void setAssignedUser(Long assignedUser) {
        this.assignedUser = assignedUser;
    }

    public void setCurrentRating(float currentRating) {
        this.currentRating = currentRating;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitute(double longitute) {
        this.longitute = longitute;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }
    //</editor-fold>

    public User(String authToken, long id, String firstName, String lastName, byte[] avatar, String phone, String email, String password, String facebookId, String googleId, boolean driver, String car, Point location, boolean hailing, Long assignedUser, float currentRating, double latitude, double longitute, float direction) {
        this.authToken = authToken;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.facebookId = facebookId;
        this.googleId = googleId;
        this.driver = driver;
        this.car = car;
        this.location = location;
        this.hailing = hailing;
        this.assignedUser = assignedUser;
        this.currentRating = currentRating;
        this.latitude = latitude;
        this.longitute = longitute;
        this.direction = direction;
    }

    public User(UserDAO user) {
        this.authToken = user.getAuthToken();
        this.id = user.getId();
        this.firstName = user.getGivenName();
        this.lastName = user.getSurname();
        this.avatar = null;
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.facebookId = user.getFacebookId();
        this.googleId = user.getGoogleId();
        this.driver = user.getDao().isDriver();
        this.car = user.getCar();
        this.location = null;
        //this.hailing = user.;
        this.assignedUser = null;
        this.currentRating = user.getCurrentRating();
        this.latitude = user.getLatitude();
        this.longitute = user.getLongitude();
        this.direction = user.getDirection();
    }
    public UserDAO getDao() {
        UserDAO dao=new UserDAO(id,firstName,lastName,phone,email,facebookId,googleId,driver,car,currentRating,latitude,longitute,direction,authToken,password);
        return dao;
    }

    public void setLongitude(double longitude) {
        this.longitute = longitude;
    }
}