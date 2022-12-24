package ro.ananimarius.allride.allride.userService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ro.ananimarius.allride.allride.CRUDinterfaces.UserRepository;
import ro.ananimarius.allride.allride.UserDAO.UserDAO;
import ro.ananimarius.allride.allride.user.User;

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

    public void updateUser(UserDAO user){
        User u=users.findByAuthToken(user.getAuthToken()).get(0);
        u.setCar(user.getCar());
        u.setEmail(user.getEmail());
        u.setFacebookId(user.getFacebookId());
        u.setFirstName(user.getGivenName());
        u.setLastName(user.getSurname());
        u.setGoogleId(user.getGoogleId());
        u.setLatitude(user.getLatitude());
        u.setLongitute(user.getLongitude());
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
        return loginImpl(users.findByFacebookId(googleId),password);
    }
    private UserDAO loginImpl(List<User> us,String password) throws UserAuthenticationException {
        if(us==null||us.isEmpty()){
            return null;
        }
        if(us.size()>1){
            throw new RuntimeException("Illegal state"+us.size()+" users with the same phone are listed!");
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


}
