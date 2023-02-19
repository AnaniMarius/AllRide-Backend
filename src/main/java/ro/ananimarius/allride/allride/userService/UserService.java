package ro.ananimarius.allride.allride.userService;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.ananimarius.allride.allride.CRUDinterfaces.UserRepository;
import ro.ananimarius.allride.allride.UserDAO.UserDAO;
import ro.ananimarius.allride.allride.user.User;
import ro.ananimarius.allride.allride.CRUDinterfaces.UserRepository;

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
            userRepository.save(user);
        }
    }
    public String reCreateAuthToken(String email) {
        List<User> users = userRepository.findByEmail(email);
        if (!users.isEmpty()) {
            User user = users.get(0);
            User x=new User();
            user.setAuthToken(x.getAuthToken());
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
    public String getAuthToken(String googleId){
        List<User> us = users.findByGoogleId(googleId);
        if (!us.isEmpty()) {
            return us.get(0).getAuthToken();
        } else {
            return null;
        }
    }


}
