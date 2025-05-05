package api.tests.orders;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import net.andreinc.mockneat.MockNeat;
import objects.communication.IngredientsRequest;
import objects.communication.IngredientsResponse;
import objects.communication.OrderRequest;
import objects.methods.CheckMethods;
import objects.methods.OrderMethods;
import objects.methods.UserMethods;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;

public class CreateOrderTests {

    MockNeat random = MockNeat.threadLocal();
    private String email;
    private String password;
    private String name;
    private String token;
    private List<IngredientsRequest> ingredients = new ArrayList<>();
    private final OrderMethods order = new OrderMethods();
    private final UserMethods user = new UserMethods();
    private final CheckMethods check = new CheckMethods();


    //   --- Продготавливаем тестовые данные ---
    @Before
    @Step("Preparation of test data")
    public void prepareTestData() {
        this.email = random.emails().get();
        this.password = random.passwords().get();
        this.name = random.names().get();

        Response response = user.registerUser(email, password, name);
        check.checkStatusCode(response, SC_OK);

        if (response.getStatusCode() == SC_OK) {
            token = user.getToken(response);
        }

        response = order.getIngredientList();
        check.checkStatusCode(response, SC_OK);
        ingredients = response.body().as(IngredientsResponse.class).getData();
    }


    //   --- Удаляем тестовые данные после тестирования ---
    @After
    @Step("Deleting data after the test")
    public void clearAfterTests() {
        if (token != null) {
            check.checkStatusCode(user.deleteUser(token), SC_ACCEPTED);
        }
    }


    //   --- Создание заказа с авторизацией и с ингредиентами ---
    @Test
    @DisplayName("Create an order with authorization and random ingredients")
    @Description("API test for creating an order with authorization using random ingredients from the list." +
            "The expected result is that the order has been successfully created.")
    public void createOrderWithAuthAndWithIngredientsTest() {
        int numberOfIngredients = 3;
        List<String> selectedIngredients = new ArrayList<>();
        for (int i = 0; i < numberOfIngredients && i < ingredients.size(); i++) {
            IngredientsRequest Ingredients = ingredients.get(i);
            selectedIngredients.add(Ingredients.getId());
        }
        Response response = order.createOrder(selectedIngredients, token);

        check.checkStatusCode(response, SC_OK);
        check.checkSuccessStatus(response, "true");
    }


    //   --- Создание заказа без авторизации и с ингредиентами ---
    @Test
    @DisplayName("Creating an order without authorization and using random ingredients")
    @Description("API test for creating an order with authorization using random ingredients from a list. " +
            "Expected result - the order has been successfully created.")
    public void createOrderWithoutAuthAndWithIngredientsTest() {
        int numberOfIngredients = 3;
        List<String> selectedIngredients = new ArrayList<>();
        for (int i = 0; i < numberOfIngredients && i < ingredients.size(); i++) {
            IngredientsRequest Ingredients = ingredients.get(i);
            selectedIngredients.add(Ingredients.getId());
        }
        Response response = order.createOrder(new OrderRequest(selectedIngredients));

        check.checkStatusCode(response, SC_OK);
        check.checkSuccessStatus(response, "true");
    }


    //   --- Создание заказа с авторизацией и без ингредиентов ---
    @Test
    @DisplayName("Creating an order with authorization and without ingredients")
    @Description("API test for creating an order with authorization, without adding ingredients. " +
            "Expected result - the order was not created, an error message was received.")
    public void createOrderWithAuthAndWithoutIngredientsTest() {
        List<String> emptyIngredients = new ArrayList<>();
        Response response = order.createOrder(emptyIngredients, token);

        check.checkStatusCode(response, SC_BAD_REQUEST);
        check.checkSuccessStatus(response, "false");
        check.checkMessageText(response, "Ingredient ids must be provided");
    }


    //   --- Создание заказа без авторизации и без ингредиентов ---
    @Test
    @DisplayName("Creating an order without authorization and without ingredients")
    @Description("API test for creating an order without authorization, without adding ingredients. " +
            "Expected result - the order was not created, an error message was received.")
    public void createOrderWithoutAuthAndWithoutIngredientsTest() {
        List<String> emptyIngredients = new ArrayList<>();
        Response response = order.createOrder(emptyIngredients);

        check.checkStatusCode(response, SC_BAD_REQUEST);
        check.checkSuccessStatus(response, "false");
        check.checkMessageText(response, "Ingredient ids must be provided");
    }


    //   --- Создание заказа с авторизацией и с неверным хешем ингредиентов ---
    @Test
    @DisplayName("Creating an order with authorization and with an incorrect hash of ingredients")
    @Description("API test for creating an order with authorization, with an incorrect hash of ingredients. " +
            "Expected result - the order was not created, an error message was received.")
    public void createOrderWithAuthAndWithWrongHashTest() {
        List<String> testIngredients = Arrays.asList(
                random.hashes().md2().get(),
                random.hashes().md2().get(),
                random.hashes().md2().get());
        Response response = order.createOrder(testIngredients, token);

        check.checkStatusCode(response, SC_INTERNAL_SERVER_ERROR);
    }


    //   --- Создание заказа без авторизации и с неверным хешем ингредиентов ---
    @Test
    @DisplayName("Creating an order without authorization and with an incorrect hash of ingredients")
    @Description("API test for creating an order without authorization, with an incorrect hash of ingredients. " +
            "Expected result - the order was not created, an error message was received.")
    public void createOrderWithoutAuthAndWithWrongHashTest() {
        List<String> testIngredients = Arrays.asList(
                random.hashes().md2().get(),
                random.hashes().md2().get(),
                random.hashes().md2().get());
        Response response = order.createOrder(testIngredients);

        check.checkStatusCode(response, SC_INTERNAL_SERVER_ERROR);
    }

}
