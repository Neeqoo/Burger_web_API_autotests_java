package objects.http;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BaseHTTP {

    @Step("Base request")
    private RequestSpecification baseRequest() {
        return new RequestSpecBuilder()
                .addFilter(new AllureRestAssured())
                .setRelaxedHTTPSValidation()
                .build();
    }

    @Step("Base request")
    private RequestSpecification baseRequest(String contentType) {
        return new RequestSpecBuilder()
                .addHeader("Content-type", contentType)
                .addFilter(new AllureRestAssured())
                .setRelaxedHTTPSValidation()
                .build();
    }

    @Step("Post request")
    public Response postRequest(String url, Object requestBody, String contentType) {
        return given(this.baseRequest(contentType))
                .body(requestBody)
                .when()
                .post(url);
    }

    @Step("Post request")
    public Response postRequest(String url, Object requestBody, String contentType, String token) {
        return given(this.baseRequest(contentType))
                .auth().oauth2(token)
                .body(requestBody)
                .when()
                .post(url);
    }

    @Step("Get request")
    public Response getRequest(String url) {
        return given(this.baseRequest())
                .get(url);
    }

    @Step("Get request")
    public Response getRequest(String url, String token) {
        return given(this.baseRequest())
                .auth().oauth2(token)
                .when()
                .get(url);
    }

    @Step("Delete request")
    public Response deleteRequest(String url, String token) {
        return given(this.baseRequest())
                .auth().oauth2(token)
                .delete(url);
    }

    @Step("Patch request")
    public Response patchRequest(String url, Object requestBody, String contentType, String token) {
        return given(this.baseRequest(contentType))
                .auth().oauth2(token)
                .body(requestBody)
                .when()
                .patch(url);
    }

    @Step("Patch request")
    public Response patchRequest(String url, Object requestBody, String contentType) {
        return given(this.baseRequest(contentType))
                .body(requestBody)
                .when()
                .patch(url);
    }

}
