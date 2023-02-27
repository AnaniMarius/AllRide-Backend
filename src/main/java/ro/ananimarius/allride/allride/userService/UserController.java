package ro.ananimarius.allride.allride.userService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.ananimarius.allride.allride.UserDAO.UserDAO;
import ro.ananimarius.allride.allride.user.DriverDTO;
import ro.ananimarius.allride.allride.user.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService users;

    @ExceptionHandler(
            UserAuthenticationException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)

    public @ResponseBody ErrorDAO handleException(
            UserAuthenticationException e) {
        return new ErrorDAO("Invalid Password",
                ErrorDAO.ERROR_INVALID_PASSWORD);
    }

    @RequestMapping(method= RequestMethod.GET, value = "/exists")
    public @ResponseBody boolean exists(String phone){
        return users.existsByPhone(phone);
    }
    @RequestMapping(method = RequestMethod.POST, value = "/loginByGoogle")
    public ResponseEntity<String> loginByGoogle(@RequestParam("idToken") String idToken,
                                                @RequestParam("email") String email,
                                                @RequestParam("familyName") String fName,
                                                @RequestParam("givenName") String gName,
                                                @RequestParam("isDriver") Boolean isDriver,
                                                @RequestParam("latitude") double latitude,
                                                @RequestParam("longitude") double longitude) {
        boolean existsByGoogleId = users.existsByGoogleId(idToken);
        //boolean existsByEmail = users.existsByEmail(email);
        if (existsByGoogleId) {
            //set the lat and long upon login
            users.setLatLong(idToken,latitude,longitude);
            //CHECK IF ALREADY IS AUTHTOKEN. IF YES, REJECT THE REQUEST.
            if(users.getAuthToken(idToken)==null){ //verific daca authtoken nu exista (nu ar trebui), dar totusi daca exista, provizoriu sa ma logheze anyways
                String reToken=users.reCreateAuthToken(email, isDriver); //PROVIZORIU set driver status
                JSONObject responseJson = new JSONObject();
                responseJson.put("authToken", reToken);
                return ResponseEntity.ok().body(responseJson.toString());
            }
            else {
                //create a new authToken !!!! modify, here is just giving it the existing one
                String authToken = users.getAuthToken(idToken);
                JSONObject responseJson = new JSONObject();
                responseJson.put("authToken", authToken);
                return ResponseEntity.ok().body(responseJson.toString());
            }
        } else {
            //hser doesn't exist, create a new user object using the UserDAO class
            User x=new User();
            x.setDriver(isDriver);
            UserDAO user = new UserDAO();
            user.setAuthToken(x.getAuthToken());
            user.setGivenName(gName);
            user.setSurname(fName);
            user.setEmail(email);
            user.setPhone("");
            user.setFacebookId("");
            user.setGoogleId(idToken);
            user.setDriver(isDriver); //bugged & fixed above
            user.setPassword("");
            user.setLatitude(latitude);
            user.setLongitude(longitude);
            user.setId(12L);

            //add the new user to the database using the UserService class
            String authToken = users.addUser(user);
            JSONObject responseJson = new JSONObject();
            responseJson.put("authToken", authToken);
            //success response
            return ResponseEntity.ok().body(responseJson.toString());
        }
    }
    @RequestMapping(method = RequestMethod.POST, value="/updateLocation")
    public @ResponseBody ResponseEntity<String> updateLocation(@RequestParam String authToken,
                                                               @RequestParam("idToken") String idToken,
                                                               @RequestParam("latitude") double latitude,
                                                               @RequestParam("longitude") double longitude){
        boolean userAuthorized=users.authUser(idToken, authToken);
        if(userAuthorized) {
            users.setLatLong(idToken, latitude, longitude);
            return ResponseEntity.ok().body("Location Updated Successfully!");
        }
        return ResponseEntity.ok().body("Failed updating the location!");
    }

    @RequestMapping(method=RequestMethod.GET,value = "/login")
    public @ResponseBody UserDAO login( @RequestParam(value="password", required=true) String password,
                                        String phone, String googleId, String facebookId)
            throws UserAuthenticationException {
        if(phone != null) {
            return users.loginByPhone(phone, password);
        }
        if(facebookId != null) {
            return users.loginByFacebook(facebookId, password);
        }
        if(googleId != null) {
            return users.loginByGoogle(googleId, password);
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/signout")
    public String signout(@RequestBody String authToken) {
        // Call the clearAuthToken method of the user service
        String authTokenParsed=null;
        String[] parts = authToken.split("=");
        authTokenParsed = parts[1];
        users.clearAuthToken(authTokenParsed); //also clears the isDriver
        return "Signout successful";
    }

        public class DriverSelectResult {
        private List<DriverDTO> drivers;
        private String message;
        public DriverSelectResult(List<DriverDTO> drivers, String message) {this.drivers = drivers;this.message = message;}
            public List<DriverDTO> getDrivers() {return drivers;}
            public void setDrivers(List<DriverDTO> drivers) {this.drivers = drivers;}
            public String getMessage() {return message;}
            public void setMessage(String message) {this.message = message;}
        }
//    @RequestMapping(method = RequestMethod.POST, value="/selectDriver") //the traditional way of selecting from a list of drivers in the radius
//    public @ResponseBody DriverSelectResult selectDriver(@RequestParam String authToken,
//                                                 @RequestParam("idToken") String idToken,
//                                                 @RequestParam("latitude") double latitude,
//                                                 @RequestParam("longitude") double longitude){
//        boolean userAuthorized=users.authUser(idToken, authToken);
//        DriverSelectResult result=null;
//        if(userAuthorized) {
//            List<DriverDTO> drivers = users.searchDrivers(latitude, longitude);
//
//            if(drivers!=null){
//                result= new DriverSelectResult(drivers,"Drivers found");
//            }
//            else {
//                result = new DriverSelectResult(null, "Available drivers not found in the city");
//            }
//            return result;
//        }
//        else{
//            result = new DriverSelectResult(null, "User not authorized!");
//            return result;
//        }
//    }
@RequestMapping(method = RequestMethod.POST, value="/selectDriver") //the traditional way of selecting from a list of drivers in the radius
public @ResponseBody List<DriverDTO> selectDriver(@RequestParam String authToken,
                                                     @RequestParam("idToken") String idToken,
                                                     @RequestParam("latitude") double latitude,
                                                     @RequestParam("longitude") double longitude){
    boolean userAuthorized=users.authUser(idToken, authToken);
    List<DriverDTO> drivers=new ArrayList<>();
    if(userAuthorized) {
         drivers = users.searchDrivers(latitude, longitude);

        if(drivers!=null){
            return drivers;
        }
        return drivers;
    }
    else{
        return drivers;
    }
}
    @RequestMapping(method = RequestMethod.POST, value="/requestDriver") //with machine learning
    public void requestDriver(@RequestParam String authToken,
                             @RequestParam("idToken") String idToken,
                             @RequestParam("latitude") double latitude,
                             @RequestParam("longitude") double longitude){
        boolean userAuthorized=users.authUser(idToken, authToken);
        if(userAuthorized) {
            //compete here
        }
    }

    @RequestMapping(method = RequestMethod.POST, value="/onMapDrivers") //show all the drivers on the map, later will modify to display only within radius
    public @ResponseBody List<DriverDTO> onMapDrivers(@RequestParam String authToken,
                                                         @RequestParam("idToken") String idToken/*,
                                                         @RequestParam("latitude") double latitude,
                                                         @RequestParam("longitude") double longitude*/){
        boolean userAuthorized=users.authUser(idToken, authToken);
        DriverSelectResult result=null;
        List<DriverDTO> drivers=new ArrayList<>();
        if(userAuthorized) {
            drivers = users.displayAvailableDrivers(/*latitude, longitude*/);

            if(drivers!=null){
                //result= new DriverSelectResult(drivers,"Drivers found");
                return drivers;
            }
            else {
                return drivers;
                //result = new DriverSelectResult(null, "Available drivers not found");
            }
            //return result;
        }
        else{
            //result = new DriverSelectResult(null, "User not authorized!");
            //return result;
            return drivers;
        }
    }

    @RequestMapping(method = RequestMethod.POST,value = "/add")
    public @ResponseBody String addEditUser(@RequestBody UserDAO ud)
            throws IOException {
        if(ud.getId() != null) {
            users.updateUser(ud);
            return ud.getId().toString();
        } else {
            return users.addUser(ud);
        }
    }

    @RequestMapping(value = "/avatar/{id:.+}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getAvatar(@PathVariable("id") Long id) {
        byte[] av = users.getAvatar(id);
        if(av != null) {
            return ResponseEntity.ok().
                    contentType(MediaType.IMAGE_JPEG).
                    body(av);
        }
        return ResponseEntity.notFound().build();
    }
    @RequestMapping(method = RequestMethod.POST,
            value = "/updateAvatar/{auth:.+}")
    public @ResponseBody String updateAvatar(@PathVariable("auth") String auth, @RequestParam(name="img", required = true) MultipartFile img) throws IOException {
        users.setAvatar(auth, img.getBytes());
        return "OK";
    }
}