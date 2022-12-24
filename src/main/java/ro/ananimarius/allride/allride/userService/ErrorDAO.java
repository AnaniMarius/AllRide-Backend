package ro.ananimarius.allride.allride.userService;

import org.springframework.beans.factory.annotation.Autowired;

public class ErrorDAO {
    public static int ERROR_INVALID_PASSWORD;
    @Autowired
    private UserService users;
    private String error;
    private int code;

    public ErrorDAO() {
    }

    public ErrorDAO(String error, int code) {
        this.error = error;
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
