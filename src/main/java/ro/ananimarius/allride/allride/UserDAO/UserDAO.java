package ro.ananimarius.allride.allride.UserDAO;

import java.io.Serializable;

public class UserDAO implements Serializable {
    private Long id;
    private String givenName;
    private String surname;
    private String phone;
    private String email;
    private String facebookId;
    private String googleId;
    private boolean driver;
    private String car;
    private float currentRating;
    private double latitude;
    private double longitude;
    private float direction;
    private String authToken;
    private String password;

    public UserDAO(Long id, String givenName, String surname, String phone, String email, String facebookId, String googleId, boolean driver, String car, float currentRating, double latitude, double longitude, float direction) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public boolean isDriver() {
        return driver;
    }

    public void setDriver(boolean driver) {
        this.driver = driver;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public float getCurrentRating() {
        return currentRating;
    }

    public void setCurrentRating(float currentRating) {
        this.currentRating = currentRating;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserDAO() {
    }

    public UserDAO(Long id, String givenName, String surname, String phone, String email, String facebookId, String googleId, boolean driver, String car, float currentRating, double latitude, double longitude, float direction, String authToken, String password) {
        this.id = id;
        this.givenName = givenName;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.facebookId = facebookId;
        this.googleId = googleId;
        this.driver = driver;
        this.car = car;
        this.currentRating = currentRating;
        this.latitude = latitude;
        this.longitude = longitude;
        this.direction = direction;
        this.authToken = authToken;
        this.password = password;
    }
    public UserDAO getDao() {
        return new UserDAO(id, givenName, surname, phone, email, facebookId,
                googleId, driver, car, currentRating, latitude,
                longitude, direction);
    }
    public UserDAO getPartialDao() {
        return new UserDAO(id, givenName, surname, null, null, null, null,
                driver, car, currentRating, latitude, longitude, direction);
    }


}
