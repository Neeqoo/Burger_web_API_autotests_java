package objects.http;

import io.restassured.response.Response;
import objects.communication.OrderRequest;
import objects.resource.Api;
import objects.resource.Urls;

public class OrderHTTP extends BaseHTTP {

    public Response createOrder(OrderRequest order, String token) {
        return postRequest(
                Urls.SERVER_NAME + Api.ORDERS,
                order,
                "application/json",
                token
        );
    }

    public Response createOrder(OrderRequest order) {
        return postRequest(
                Urls.SERVER_NAME + Api.ORDERS,
                order,
                "application/json"
        );
    }


    public Response getIngredientList() {
        return getRequest(
                Urls.SERVER_NAME + Api.INGREDIENTS
        );
    }

    public Response getOrderList(String token) {
        return getRequest(
                Urls.SERVER_NAME + Api.ORDERS,
                token
        );
    }

    public Response getAllOrderList() {
        return getRequest(
                Urls.SERVER_NAME + Api.ALL_ORDERS
        );
    }

}
