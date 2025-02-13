package objects.communication;

public class UserResponse {

    private String success;
    private UserRequest user;
    private String accessToken;
    private String message;


    public String getSuccess() {
        return success;
    }

    public UserRequest getUser() {
        return user;
    }

    public String getAccessToken() {
        return accessToken;
    }


    public String getMessage() {
        return message;
    }


}
