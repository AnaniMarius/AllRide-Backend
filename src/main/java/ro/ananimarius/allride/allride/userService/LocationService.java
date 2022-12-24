package ro.ananimarius.allride.allride.userService;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import ro.ananimarius.allride.allride.CRUDinterfaces.UserRepository;
import ro.ananimarius.allride.allride.UserDAO.UserDAO;
import ro.ananimarius.allride.allride.user.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationService {
    @Autowired
    private UserRepository users;

    public void updateUserLocation(String token, double lat,
                                   double lon, float dir) {
        List<User> us = users.findByAuthToken(token);
        User u = us.get(0);
        u.setLatitude(lat);
        u.setLongitude(lat);
        u.setDirection(dir);
        users.save(u);
    }

    public List<UserDAO> findAllDrivers(double lat, double lon,
                                        double radius) {
        double minLat = lat - radius * 0.009044;
        double minLon = lon - radius * 0.0089831;
        double maxLat = lat + radius * 0.009044;
        double maxLon = lon + radius * 0.0089831;
        return toDaoList(
                users.findByDriver(minLat, maxLat, minLon, maxLon));
    }

    public List<UserDAO> findAvailableDrivers(double lat, double lon,
                                              double radius) {
        double minLat = lat - radius * 0.009044;
        double minLon = lon - radius * 0.0089831;
        double maxLat = lat + radius * 0.009044;
        double maxLon = lon + radius * 0.0089831;
        return toDaoList(users.findByAvailableDriver(minLat, maxLat,
                minLon, maxLon));
    }

    private List<UserDAO> toDaoList(List<User> us) {
        ArrayList<UserDAO> respone = new ArrayList<>();
        for(User u : us) {
            respone.add(u.getDao()); //GET PARTIALDAO HERE, BUT I SHOUDL DO IT
        }
        return respone;
    }
}
