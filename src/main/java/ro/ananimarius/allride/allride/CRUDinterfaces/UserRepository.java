package ro.ananimarius.allride.allride.CRUDinterfaces;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.ananimarius.allride.allride.user.User;

import java.util.List;
@Repository
//@EnableAutoConfiguration
//@Configuration
//@ComponentScan
public interface UserRepository extends CrudRepository<User,Long> {
    @Query("SELECT u FROM User u WHERE u.authToken = ?1")
    public List<User> findByAuthToken(String authToken);
    public List<User> findByPhone(String phone);
    public List<User> findByGoogleId(String googleId);
    public List<User> findByFacebookId(String facebookId);
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    public List<User> findByEmail(String email);
    @Query("select b from User b where b.driver = true and b.latitude " + "between ?1 and ?2 and b.longitude between ?3 and ?4")
    public List<User> findByDriver(double minLat, double maxLat, double minLon, double maxLon);

    @Query("select b from User b where b.driver = true and " + "b.assignedUser is null and b.latitude between ?1 and ?2 " +
            "and b.longitude between ?3 and ?4")
    public List<User> findByAvailableDriver(double minLat, double maxLat, double minLon, double maxLon);

    @Query("SELECT u FROM User u WHERE u.authToken IS NOT NULL AND u.driver = true AND " +
            "6371 * acos(cos(radians(:latitude)) * cos(radians(u.latitude)) * " +
            "cos(radians(u.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(u.latitude))) <= :radiusKm")
    public List<User> findInRadius(double latitude, double longitude, double radiusKm);

    @Query("SELECT u FROM User u WHERE u.authToken IS NOT NULL AND u.driver = true")
    public List<User> findAvailableDrivers();
}
