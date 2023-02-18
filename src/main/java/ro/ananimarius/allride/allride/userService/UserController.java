package ro.ananimarius.allride.allride.userService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.ananimarius.allride.allride.UserDAO.UserDAO;
import ro.ananimarius.allride.allride.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
                                                @RequestParam("email") String email) {
        boolean exists = users.existsByGoogleId(idToken);
        if (exists) {
            //response for user existing already
            String authToken = users.getAuthToken(idToken);
            JSONObject responseJson = new JSONObject();
            responseJson.put("authToken", authToken);
            return ResponseEntity.ok().body(responseJson.toString());
        } else {
            //hser doesn't exist, create a new user object using the UserDAO class
            User x=new User();
            UserDAO user = new UserDAO();
            user.setAuthToken(x.getAuthToken());
            user.setGivenName("");
            user.setSurname("");
            user.setEmail(email);
            user.setPhone("");
            user.setFacebookId("");
            user.setGoogleId(idToken);
            user.setPassword("");
            user.setId(12L);
            //
            //add the new user to the database using the UserService class
            String authToken = users.addUser(user);
            JSONObject responseJson = new JSONObject();
            responseJson.put("authToken", authToken);
            //success response
            return ResponseEntity.ok().body(responseJson.toString());
        }
    }
    @RequestMapping(method=RequestMethod.GET,value = "/login")
    public @ResponseBody UserDAO login(
                                        @RequestParam(value="password", required=true) String password,
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

@RequestMapping(value = "/signout", method = RequestMethod.GET) //TO BE WORKED ON !!!!!!!!!!
public String signout(HttpServletRequest request) {
    //get the auth token from the security context
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String authToken = authentication.getCredentials().toString();

    //call the clearAuthToken method of the user service
    users.clearAuthToken(authToken);

    //invalidate the session
    HttpSession session = request.getSession(false);
    if (session != null) {
        session.invalidate();
    }
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
