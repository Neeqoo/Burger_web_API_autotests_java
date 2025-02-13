package objects.http;

import io.restassured.response.Response;
import objects.communication.UserRequest;
import objects.resource.Api;
import objects.resource.Urls;

public class UserHTTP extends BaseHTTP {

    public Response registerUser(UserRequest user) {
        return postRequest(
                Urls.SERVER_NAME + Api.REGISTER_USER,
                user,
                "application/json"
        );
    }
    public Response deleteUser(String token) {
        return deleteRequest(
                Urls.SERVER_NAME + Api.USER,
                token
        );
    }
    public Response loginUser(UserRequest user) {
        return postRequest(
                Urls.SERVER_NAME + Api.LOGIN_USER,
                user,
                "application/json"
        );
    }
    public Response updateUser(UserRequest user, String token) {
        return patchRequest(
                Urls.SERVER_NAME + Api.USER,
                user,
                "application/json",
                token
        );
    }
    public Response updateUser(UserRequest user) {
        return patchRequest(
                Urls.SERVER_NAME + Api.USER,
                user,
                "application/json"
        );
    }

}
