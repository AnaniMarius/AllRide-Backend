package ro.ananimarius.allride.allride;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import ro.ananimarius.allride.allride.CRUDinterfaces.UserRepository;
import ro.ananimarius.allride.allride.user.User;
import ro.ananimarius.allride.allride.userService.LocationService;
import ro.ananimarius.allride.allride.userService.UserService;

import java.util.List;
import java.util.Optional;

@Configuration
public class myConfig {
//    @Bean
//    public LocalEntityManagerFactoryBean entityManagerFactory() {
//        LocalEntityManagerFactoryBean factoryBean = new LocalEntityManagerFactoryBean();
//        factoryBean.setPersistenceUnitName("myPersistenceUnit");
//        return factoryBean;
//    }

    @Bean
    public UserService userService(){
        return new UserService();
    }

    @Bean
    public UserRepository userRepository(){
        return new UserRepository() {
            @Override
            public List<User> findByAuthToken(String authToken) {
                return null;
            }

            @Override
            public List<User> findByPhone(String phone) {
                return null;
            }

            @Override
            public List<User> findByGoogleId(String googleId) {
                return null;
            }

            @Override
            public List<User> findByFacebookId(String facebookId) {
                return null;
            }

            @Override
            public List<User> findByDriver(double minLat, double maxLat, double minLon, double maxLon) {
                return null;
            }

            @Override
            public List<User> findByAvailableDriver(double minLat, double maxLat, double minLon, double maxLon) {
                return null;
            }

            @Override
            public <S extends User> S save(S entity) {
                return null;
            }

            @Override
            public <S extends User> Iterable<S> saveAll(Iterable<S> entities) {
                return null;
            }

            @Override
            public Optional<User> findById(Long aLong) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(Long aLong) {
                return false;
            }

            @Override
            public Iterable<User> findAll() {
                return null;
            }

            @Override
            public Iterable<User> findAllById(Iterable<Long> longs) {
                return null;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(Long aLong) {

            }

            @Override
            public void delete(User entity) {

            }

            @Override
            public void deleteAllById(Iterable<? extends Long> longs) {

            }

            @Override
            public void deleteAll(Iterable<? extends User> entities) {

            }

            @Override
            public void deleteAll() {

            }
        };
    }
    @Bean
    public LocationService locationService(){
        return new LocationService();
    }
}




