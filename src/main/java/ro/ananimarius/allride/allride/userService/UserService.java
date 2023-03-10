package ro.ananimarius.allride.allride.userService;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.ananimarius.allride.allride.CRUDinterfaces.UserRepository;
import ro.ananimarius.allride.allride.UserDAO.UserDAO;
import ro.ananimarius.allride.allride.user.DriverDTO;
import ro.ananimarius.allride.allride.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class UserService {
    @Autowired
    private UserRepository users;

    @Autowired
    private PasswordEncoder encoder; //hashes and salts the passwords; even tho a hacker gets access to the db, it won't be able to use the passwords

    String addUser(UserDAO user){
        User u=new User(user);
        u.setPassword(encoder.encode(user.getPassword()));
        users.save(u);
        return u.getAuthToken();
    }
    @Autowired
    private UserRepository userRepository;
    //to log out
    public void clearAuthToken(String authToken) {
        List<User> users = userRepository.findByAuthToken(authToken);
        if (!users.isEmpty()) {
            User user = users.get(0);
            user.setAuthToken(null);
            user.setDriver(false); //at least temporarily
            userRepository.save(user);
        }
    }
    public String reCreateAuthToken(String email, Boolean isDriver) {
        List<User> users = userRepository.findByEmail(email);
        if (!users.isEmpty()) {
            User user = users.get(0);
            User x=new User();
            user.setAuthToken(x.getAuthToken());
            user.setDriver(isDriver);
            userRepository.save(user);
            return users.get(0).getAuthToken();
        }
        else {
            return null;
        }
    }

    public void updateUser(UserDAO user){
        User u=users.findByAuthToken(user.getAuthToken()).get(0);
        u.setCar(user.getCar());
        u.setEmail(user.getEmail());
        u.setFacebookId(user.getFacebookId());
        u.setFirstName(user.getGivenName());
        u.setLastName(user.getSurname());
        u.setGoogleId(user.getGoogleId());
        u.setLatitude(user.getLatitude());
        u.setLongitude(user.getLongitude());
        u.setPhone(user.getPhone());
        users.save(u);
    }

    public byte[] getAvatar(Long id){
        Optional<User> u=users.findById(id);
        return u.get().getAvatar();
    }

    public void setAvatar(String token, byte[] a){
        User u=users.findByAuthToken(token).get(0);
        u.setAvatar(a);
        users.save(u);
    }


    public UserDAO loginByPhone(String phone, String password) throws UserAuthenticationException{
        return loginImpl(users.findByPhone(phone),password);
    }
    public UserDAO loginByFacebook(String facebookId, String password) throws UserAuthenticationException{
        return loginImpl(users.findByFacebookId(facebookId),password);
    }
    public UserDAO loginByGoogle(String googleId, String password) throws UserAuthenticationException{
        return loginImpl(users.findByGoogleId(googleId),password);
    }
    private UserDAO loginImpl(List<User> us,String password) throws UserAuthenticationException {
        if(us==null||us.isEmpty()){
            return null;
        }
        if(us.size()>1){
            throw new RuntimeException("Illegal state "+us.size()+" users with the same phone are listed!");
        }
        User u=us.get(0);
        if(!encoder.matches(password,u.getPassword())){
            throw new UserAuthenticationException();
        }
        UserDAO d=u.getDao();
        d.setAuthToken(u.getAuthToken());
        return d;
    }
    public boolean existsByPhone(String phone){
        List<User>us=users.findByPhone(phone);
        return !us.isEmpty();
    }
    public boolean existsByGoogleId(String googleId){
        List<User>us=users.findByGoogleId(googleId);
        return !us.isEmpty();
    }
    public boolean existsByEmail(String email){
        List<User>us=users.findByEmail(email);
        return !us.isEmpty();
    }
    public String getAuthToken(String googleId){
        List<User> us = users.findByGoogleId(googleId);
        if (!us.isEmpty()) {
            return us.get(0).getAuthToken();
        } else {
            return null;
        }
    }

    public void setLatLong(String googleId, Double latitude, Double longitude){
        List<User> users = userRepository.findByGoogleId(googleId);
        if (!users.isEmpty()) {
            User user = users.get(0);
            user.setLatitude(latitude);
            user.setLongitude(longitude);
            userRepository.save(user);
        }
    }
    public boolean authUser(String googleId, String authToken) {
        List<User> users = userRepository.findByGoogleId(googleId);
        if (users.isEmpty()) {
            return false;
        } else {
            User user = users.get(0);
            return user.getAuthToken().equals(authToken);
        }
    }

    public List<DriverDTO> searchDrivers(double latitude, double longitude) {
        List<User> users=new ArrayList<>();
        for(double radiusKm=1;radiusKm<=100000;radiusKm++){
            users = userRepository.findInRadius(latitude, longitude, radiusKm);
            if(users.size()>=1){
                break;
            }
        }
        if (users.isEmpty()||users==null) {
            return null;
        } else {
            List<DriverDTO> drivers = new ArrayList<>();
            for (User user : users) {
                DriverDTO driver = new DriverDTO(user.getFirstName(),user.getLastName(),user.getPhone(),user.getGoogleId(),user.getLatitude(),
                        user.getLongitude(),user.getCar(),user.getCurrentRating());
                drivers.add(driver);
            }
            return drivers;
        }
    }
    public List<DriverDTO> displayAvailableDrivers() {
        List<User> users=new ArrayList<>();
        while(users.size()==0){
            users = userRepository.findAvailableDrivers();
        }
        if (users.isEmpty()) {
            return null;
        } else {
            List<DriverDTO> drivers = new ArrayList<>();
            for (User user : users) {
                DriverDTO driver = new DriverDTO(user.getLatitude(), user.getLongitude());
                drivers.add(driver);
            }
            return drivers;
        }
    }
}
