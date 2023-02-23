package ro.ananimarius.allride.allride.userService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.ananimarius.allride.allride.UserDAO.UserDAO;
import ro.ananimarius.allride.allride.user.User;

import java.io.IOException;
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
        boolean exists = users.existsByGoogleId(idToken);
        if (exists) {
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
            UserDAO user = new UserDAO();
            user.setAuthToken(x.getAuthToken());
            user.setGivenName(gName);
            user.setSurname(fName);
            user.setEmail(email);
            user.setPhone("");
            user.setFacebookId("");
            user.setGoogleId(idToken);
            user.setDriver(isDriver);
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
        boolean v=users.authUser(idToken, authToken);
        if(v) {
            users.setLatLong(idToken, latitude, longitude);
        }
        return ResponseEntity.ok().body("Location Updated Successfully!");
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
    public ResponseEntity<byte[]> getAvatar(
                                             @PathVariable("id") Long id) {
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
