package objects.methods;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import objects.communication.OrderRequest;
import objects.http.OrderHTTP;

import java.util.List;

public class OrderMethods extends OrderHTTP {

    @Step("Creating an order by an authorized user")
    public Response createOrder(List<String> ingredients, String token) {
        return super.createOrder(new OrderRequest(ingredients), token);
    }

    @Step("Creating an order by an unauthorized user")
    public Response createOrder(List<String> ingredients) {
        return super.createOrder(new OrderRequest(ingredients));
    }

    @Step("Get a list of ingredients")
    public Response getIngredientList() {
        return super.getIngredientList();
    }

    @Step("Get a list of orders")
    public Response getOrderList(String token) {
        return super.getOrderList(token);
    }

    @Step("Get a list of all orders")
    public Response getAllOrderList() {
        return super.getAllOrderList();
    }

}
