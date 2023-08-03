package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static api.Spec.*;
import static io.restassured.RestAssured.*;

public class RequestUtils {
    public static Response sendGetRequest(String endpoint) {
        return given()
                .when()
                .get(endpoint)
                .then().log().all()
                .extract().response();
    }

    public static <T> T sendPostRequest(String endpoint, Object requestBody, Class<T> responseClass) {
        Response response = given()
                .body(requestBody)
                .when()
                .post(endpoint)
                .then().log().all()
                .extract().response();

        return response.as(responseClass);
    }

    public static Response sendDeleteRequest(String endpoint){
        return given()
                .when()
                .delete(endpoint)
                .then().log().all()
                .extract().response();
    }
}
