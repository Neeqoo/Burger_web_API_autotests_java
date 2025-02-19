package api.tests.users;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import objects.methods.CheckMethods;
import objects.methods.UserMethods;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.andreinc.mockneat.MockNeat;

import java.util.ArrayList;

import static org.apache.http.HttpStatus.*;

public class CreateUserTests {

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

    }

    //   --- Удаляем тестовые данные после тестирования ---
    @After
    @Step("Deleting data after the test")
    public void clearAfterTests() {
        if (token != null) {
            check.checkStatusCode(user.deleteUser(token), SC_ACCEPTED);
        }
    }

    //   --- Регистрация нового пользователя ---
    @Test
    @DisplayName("Create new user")
    @Description("API test creating a new user. The expected result is that the user has been created")
    public void createNewUserIsSuccessTest() {
        Response response = user.registerUser(email, password, name);
        if (response.getStatusCode() == SC_OK) {
            tokens.add(user.getToken(response));
        }
        check.checkStatusCode(response, SC_OK);
        check.checkSuccessStatus(response, "true");
    }

    //   --- Регистрация двух новых пользователей с одинаковыми данными ---
    @Test
    @DisplayName("Registration of two users with the same data")
    @Description("API test creating two users with the same data" +
            "Expected result - identical users cannot be created")
    public void createNewSameTwoUsersIsFailedTest() {
        Response user1 = user.registerUser(email, password, name);
        Response user2 = user.registerUser(email, password, name);
        if (user1.getStatusCode() == SC_OK) {
            tokens.add(user.getToken(user1));
        }
        if (user2.getStatusCode() == SC_OK) {
            tokens.add(user.getToken(user2));
        }
        check.checkStatusCode(user2, SC_FORBIDDEN);
        check.checkSuccessStatus(user2, "false");
        check.checkMessageText(user2, "User already exists");
    }

    //   --- Регистрация пользователя без email ---
    @Test
    @DisplayName("User registration without email")
    @Description("API test user registration without email. The expected result is that you cannot create a user without an email")
    public void createNewUserWithoutEmailIsFailedTest() {
        Response response = user.registerUser(null, password, name);
        if (response.getStatusCode() == SC_OK) {
            tokens.add(user.getToken(response));
        }
        check.checkStatusCode(response, SC_FORBIDDEN);
        check.checkSuccessStatus(response, "false");
        check.checkMessageText(response, "Email, password and name are required fields");
    }

    //   --- Регистрация пользователя без пароля ---
    @Test
    @DisplayName("User registration without a password")
    @Description("API test registering a user without a password. The expected result is that you cannot create a user without a password")
    public void createNewUserWithoutPasswordIsFailedTest() {
        Response response = user.registerUser(email, null, name);
        if (response.getStatusCode() == SC_OK) {
            tokens.add(user.getToken(response));
        }
        check.checkStatusCode(response, SC_FORBIDDEN);
        check.checkSuccessStatus(response, "false");
        check.checkMessageText(response, "Email, password and name are required fields");
    }

    //   --- Регистрация пользователя без имени ---
    @Test
    @DisplayName("Registering a user without a name")
    @Description("API test registration of a user without a name. The expected result is that you cannot create a user without a name")
    public void createNewUserWithoutNameIsFailedTest() {
        Response response = user.registerUser(email, null, name);
        if (response.getStatusCode() == SC_OK) {
            tokens.add(user.getToken(response));
        }
        check.checkStatusCode(response, SC_FORBIDDEN);
        check.checkSuccessStatus(response, "false");
        check.checkMessageText(response, "Email, password and name are required fields");
    }

    //   --- Регистрация пользователя без данных ---
    @Test
    @DisplayName("User registration without data")
    @Description("API test user registration without data. The expected result is that you cannot create a user without data")
    public void createNewUserWithoutDataIsFailedTest() {
        Response response = user.registerUser(null, null, null);
        if (response.getStatusCode() == SC_OK) {
            tokens.add(user.getToken(response));
        }
        check.checkStatusCode(response, SC_FORBIDDEN);
        check.checkSuccessStatus(response, "false");
        check.checkMessageText(response, "Email, password and name are required fields");
    }

}
