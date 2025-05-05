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

import static org.apache.http.HttpStatus.*;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class ChangingUsersDataTests {

    MockNeat random = MockNeat.threadLocal();
    private String email;
    private String password;
    private String name;
    private String token;
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
        check.checkStatusCode(response, SC_OK);
        if (response.getStatusCode() == SC_OK) {
            token = user.getToken(response);
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

    //   --- Изменение email пользователя с авторизацией ---
    @Test
    @DisplayName("Changing the user's email address with authorization")
    @Description("API test editing the email of an authorized user. " +
            "Expected result - email changed")
    public void changeUserEmailWithAuthIsSuccessTest() {
        String newEmail = "2" + email;
        Response response = user.updateUser(newEmail, password, name, token);
        check.checkStatusCode(response, SC_OK);
        check.checkSuccessStatus(response, "true");
        user.checkUserData(response, newEmail, password, name);
    }


    //   --- Изменение имени пользователя с авторизацией ---
    @Test
    @DisplayName("Changing the username with authorization")
    @Description("API test editing the name of an authorized user. " +
            "Expected result - name changed")
    public void changeUserNameWithAuthIsSuccessTest() {
        String newName = "2" + name;
        Response response = user.updateUser(email, password, newName, token);
        check.checkStatusCode(response, SC_OK);
        check.checkSuccessStatus(response, "true");
        user.checkUserData(response, email, password, newName);
    }

    //   --- Изменение email пользователя без авторизации ---
    @Test
    @DisplayName("Changing a user's email without authorization")
    @Description("API test editing an unauthorized user's email. " +
            "Expected result - email has not been changed, an error message has been received")
    public void changeUserEmailWithoutAuthIsSuccessTest() {
        String newEmail = "2" + email;
        Response response = user.updateUser(newEmail, password, name);
        check.checkStatusCode(response, SC_UNAUTHORIZED);
        check.checkSuccessStatus(response, "false");
        check.checkMessageText(response, "You should be authorised");
    }


    //   --- Изменение имени пользователя без авторизации ---
    @Test
    @DisplayName("Changing the username without authorization")
    @Description("API test editing the name of an unauthorized user. " +
            "Expected result - the name has not been changed, an error message has been received")
    public void changeUserNameWithoutAuthIsSuccessTest() {
        String newName = "2" + name;
        Response response = user.updateUser(email, password, newName);
        check.checkStatusCode(response, SC_UNAUTHORIZED);
        check.checkSuccessStatus(response, "false");
        check.checkMessageText(response, "You should be authorised");
    }

}
