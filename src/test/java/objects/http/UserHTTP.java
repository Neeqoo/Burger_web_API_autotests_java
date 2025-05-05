package objects.http;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import objects.communication.UserRequest;
import objects.resource.Api;
import objects.resource.Urls;

public class UserHTTP extends BaseHTTP {

    @Step("User registration")
    public Response registerUser(UserRequest user) {
        return postRequest(
                Urls.SERVER_NAME + Api.REGISTER_USER,
                user,
                "application/json"
        );
    }

    @Step("Deleting a user")
    public Response deleteUser(String token) {
        return deleteRequest(
                Urls.SERVER_NAME + Api.USER,
                token
        );
    }

    @Step("User's login")
    public Response loginUser(UserRequest user) {
        return postRequest(
                Urls.SERVER_NAME + Api.LOGIN_USER,
                user,
                "application/json"
        );
    }

    @Step("Updating user data")
    public Response updateUser(UserRequest user, String token) {
        return patchRequest(
                Urls.SERVER_NAME + Api.USER,
                user,
                "application/json",
                token
        );
    }

    @Step("Updating user data")
    public Response updateUser(UserRequest user) {
        return patchRequest(
                Urls.SERVER_NAME + Api.USER,
                user,
                "application/json"
        );
    }

}
