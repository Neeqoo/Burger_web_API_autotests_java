package objects.http;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import objects.communication.OrderRequest;
import objects.resource.Api;
import objects.resource.Urls;

public class OrderHTTP extends BaseHTTP {

    @Step("Creating an order")
    public Response createOrder(OrderRequest order, String token) {
        return postRequest(
                Urls.SERVER_NAME + Api.ORDERS,
                order,
                "application/json",
                token
        );
    }

    @Step("Creating an order")
    public Response createOrder(OrderRequest order) {
        return postRequest(
                Urls.SERVER_NAME + Api.ORDERS,
                order,
                "application/json"
        );
    }

    @Step("Get ingredients")
    public Response getIngredientList() {
        return getRequest(
                Urls.SERVER_NAME + Api.INGREDIENTS
        );
    }

    @Step("Get order list")
    public Response getOrderList(String token) {
        return getRequest(
                Urls.SERVER_NAME + Api.ORDERS,
                token
        );
    }

    @Step("Get all orders list")
    public Response getAllOrderList() {
        return getRequest(
                Urls.SERVER_NAME + Api.ALL_ORDERS
        );
    }

}
