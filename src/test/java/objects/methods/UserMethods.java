package objects.methods;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import objects.communication.UserRequest;
import objects.communication.UserResponse;
import objects.http.UserHTTP;
import org.hamcrest.MatcherAssert;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;

public class UserMethods extends UserHTTP {

    @Step("Creating a new user")
    public Response registerUser(String email, String password, String name) {
        return super.registerUser(new UserRequest(email, password, name));
    }

    @Step("Getting an authorization token")
    public String getToken(Response response) {
        String token = response.body().as(UserResponse.class).getAccessToken().split(" ")[1];
        Allure.addAttachment("Code and status: ", response.getStatusLine());
        Allure.addAttachment("The token: ", token);
        return token;
    }

    @Step("User's login")
    public Response loginUser(String email, String password) {
        return super.loginUser(new UserRequest(email, password));
    }

    @Step("Deleting a user")
    public Response deleteUser(String token) {
        return super.deleteUser(token);
    }

    @Step("Updating user data")
    public Response updateUser(String email, String password, String name, String token) {
        return super.updateUser(new UserRequest(email, password, name), token);
    }

    @Step("Updating user data without a token")
    public Response updateUser(String email, String password, String name) {
        return super.updateUser(new UserRequest(email, password, name));
    }

    @Step("Verifying user data")
    public void checkUserData(Response response, String expectedEmail, String expectedPassword, String expectedName) {
        UserRequest currentUser = response.body().as(UserResponse.class).getUser();
        Allure.addAttachment("New user", currentUser.toString());

        MatcherAssert.assertThat("The Email doesn't match", currentUser.getEmail(), equalTo(expectedEmail));
        MatcherAssert.assertThat("The name doesn't match", currentUser.getName(), equalTo(expectedName));

        new CheckMethods().checkStatusCode(loginUser(expectedEmail, expectedPassword), SC_OK);
    }

}
