package objects.communication;

public class UserRequest {

    private String email;
    private String password;
    private String name;


    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public UserRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public UserRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
