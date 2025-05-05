package objects.methods;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import objects.communication.UserResponse;
import org.hamcrest.MatcherAssert;

import static org.hamcrest.CoreMatchers.equalTo;

public class CheckMethods {

    @Step("Checking the response code and status")
    public void checkStatusCode(Response response, int code) {
        Allure.addAttachment("\n" + "Response code and status:", response.getStatusLine());
        response.then().statusCode(code);
    }

    @Step("Request success check")
    public void checkSuccessStatus(Response response, String expectedValue) {
        MatcherAssert.assertThat(
                "Invalid value in the success field",
                expectedValue,
                equalTo(response.body().as(UserResponse.class).getSuccess())
        );
    }

    @Step("Checking the response body")
    public void checkMessageText(Response response, String expectedMessage) {
        MatcherAssert.assertThat(
                "Incorrect text in the message field",
                expectedMessage,
                equalTo(response.body().as(UserResponse.class).getMessage())
        );
    }

}
