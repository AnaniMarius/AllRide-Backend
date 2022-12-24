package ro.ananimarius.allride.allride.userService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.ananimarius.allride.allride.UserDAO.UserDAO;

import java.io.IOException;
@Controller
@RequestMapping("/user")
public class UserWebservice {
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
