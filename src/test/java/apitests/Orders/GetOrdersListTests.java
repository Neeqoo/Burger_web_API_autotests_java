package apitests.Orders;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import net.andreinc.mockneat.MockNeat;
import objects.communication.IngredientsRequest;
import objects.communication.IngredientsResponse;
import objects.methods.CheckMethods;
import objects.methods.OrderMethods;
import objects.methods.UserMethods;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;

public class GetOrdersListTests {

    MockNeat random = MockNeat.threadLocal();
    private String email;
    private String password;
    private String name;
    private String token;
    private final OrderMethods order = new OrderMethods();
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

        response = order.getIngredientList();
        check.checkStatusCode(response, SC_OK);
        List<IngredientsRequest> ingredients = response.body().as(IngredientsResponse.class).getData();

        int numberOfIngredients = 3;
        List<String> selectedIngredients = new ArrayList<>();
        for (int i = 0; i < numberOfIngredients && i < ingredients.size(); i++) {
            IngredientsRequest Ingredients = ingredients.get(i);
            selectedIngredients.add(Ingredients.get_id());
        }

        response = order.createOrder(selectedIngredients, token);
        check.checkStatusCode(response, SC_OK);
    }

    //   --- Удаляем тестовые данные после тестирования ---
    @After
    @Step("Deleting data after the test")
    public void clearAfterTests() {
        if (token != null) {
            check.checkStatusCode(user.deleteUser(token), SC_ACCEPTED);
        }
    }


    //   --- Получение списка заказов авторизованного пользователя ---
    @Test
    @DisplayName("Getting an authorized User's order list")
    @Description("API test to receive a list of orders from an authorized user" +
            "Expected result - the list of orders has been received")
    public void getAuthUsersOrdersIsSuccessTest() {
        Response response = order.getOrderList(token);
        check.checkStatusCode(response, SC_OK);
        check.checkSuccessStatus(response, "true");
    }

    //   --- Получение списка заказов неавторизованного пользователя ---
    @Test
    @DisplayName("Getting an unauthorized user's order list")
    @Description("API test for receiving a list of orders from an unauthorized user" +
            "Expected result - the list of orders was not received, an error message was received")
    public void getNotAuthUsersOrdersIsSuccess() {
        Response response = order.getOrderList("");
        check.checkStatusCode(response, SC_UNAUTHORIZED);
        check.checkSuccessStatus(response, "false");
        check.checkMessageText(response, "You should be authorised");
    }


}
