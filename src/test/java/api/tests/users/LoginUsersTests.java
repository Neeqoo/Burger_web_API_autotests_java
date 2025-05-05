package api.tests.users;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import net.andreinc.mockneat.MockNeat;
import objects.methods.CheckMethods;
import objects.methods.UserMethods;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.apache.http.HttpStatus.*;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class LoginUsersTests {

    MockNeat random = MockNeat.threadLocal();
    private String email;
    private String password;
    private String name;
    private String token;
    private ArrayList<String> tokens = new ArrayList<>();
    private final UserMethods user = new UserMethods();
    private final CheckMethods check = new CheckMethods();

    //   --- Продготавливаем тестовые данные ---
    @Before
    @Step("Preparation of test data")
    public void prepareTestData() {
        this.email = random.emails().get();
        this.password = random.passwords().get();
        this.name = random.names().full().get();
        Response response = user.registerUser(email, password, name);
        if (response.getStatusCode() == SC_OK) {
            tokens.add(user.getToken(response));
        }
    }

    //   --- Удаляем тестовые данные после тестирования ---
    @After
    @Step("Deleting data after the test")
    public void clearAfterTests() {
        if (token != null) {
            check.checkStatusCode(user.deleteUser(token), SC_ACCEPTED);
        }
    }

    //   --- Авторизация пользователя с корректными данными ---
    @Test
    @DisplayName("User authorization with correct data")
    @Description("User authorization API test with correct data. The expected result is that the user is logged in.")
    public void loginUserIsSuccessTest() {
        Response response = user.loginUser(email, password);
        check.checkStatusCode(response, SC_OK);
        check.checkSuccessStatus(response, "true");
    }

    //   --- Авторизация пользователя без email ---
    @Test
    @DisplayName("User authorization without email")
    @Description("API test user login without email. The expected result is that the user is not logged in.")
    public void loginUserWithoutEmailIsFailedTest() {
        Response response = user.loginUser(null, password);
        check.checkStatusCode(response, SC_UNAUTHORIZED);
        check.checkSuccessStatus(response, "false");
        check.checkMessageText(response, "email or password are incorrect");
    }

    //   --- Авторизация пользователя без пароля ---
    @Test
    @DisplayName("User authorization without a password")
    @Description("API test authorization of a user without a password. The expected result is that the user is not logged in.")
    public void loginUserWithoutPasswordIsFailedTest() {
        Response response = user.loginUser(email, null);
        check.checkStatusCode(response, SC_UNAUTHORIZED);
        check.checkSuccessStatus(response, "false");
        check.checkMessageText(response, "email or password are incorrect");
    }

    //   --- Авторизация пользователя с некорректным email ---
    @Test
    @DisplayName("Authorization of a user with an incorrect email address")
    @Description("API test authorization of a user with an incorrect email. The expected result is that the user is not logged in.")
    public void loginUserWithIncorrectEmailIsFailedTest() {
        Response response = user.loginUser("n" + email, password);
        check.checkStatusCode(response, SC_UNAUTHORIZED);
        check.checkSuccessStatus(response, "false");
        check.checkMessageText(response, "email or password are incorrect");
    }

    //   --- Авторизация пользователя с некорректным паролем ---
    @Test
    @DisplayName("Authorization of a user with an incorrect password")
    @Description("API test authorization of a user with an incorrect password. The expected result is that the user is not logged in.")
    public void loginUserWithIncorrectPasswordIsFailedTest() {
        Response response = user.loginUser(email, "1" + password);
        check.checkStatusCode(response, SC_UNAUTHORIZED);
        check.checkSuccessStatus(response, "false");
        check.checkMessageText(response, "email or password are incorrect");
    }

}
