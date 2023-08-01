package ApiTests;

import api.*;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;

public class ReqresTest {

    public final static String URL = "https://reqres.in/";

    @Test
    public void checkAvaterAndIdTest(){
        List<UserData> users = given()
                .when()
                .contentType(ContentType.JSON)
                .get(URL+"api/users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);
        users.forEach(x-> Assert.assertTrue(x.getAvatar().contains(x.getId().toString())));
        Assert.assertTrue(users.stream().allMatch(x->x.getEmail().endsWith("@reqres.in")));

        List<String> avatars = users.stream().map(UserData::getAvatar).collect(Collectors.toList());
        List<String> ids = users.stream().map(x->x.getId().toString()).collect(Collectors.toList());

        for (int i = 0; i< avatars.size(); i++ ){
            Assert.assertTrue(avatars.get(i).contains(ids.get(i)));
        }
    }

    @Test
    public void checkAvaterAndIdTest2(){
        Spec.installSpecification(Spec.requestSpec(URL), Spec.responseSpecOk200());
        List<UserData> users = given()
                .when()
//                .contentType(ContentType.JSON)
                .get("api/users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);
        users.forEach(x-> Assert.assertTrue(x.getAvatar().contains(x.getId().toString())));
        Assert.assertTrue(users.stream().allMatch(x->x.getEmail().endsWith("@reqres.in")));

        List<String> avatars = users.stream().map(UserData::getAvatar).collect(Collectors.toList());
        List<String> ids = users.stream().map(x->x.getId().toString()).collect(Collectors.toList());

        IntStream.range(0, avatars.size())
                .mapToObj(i -> avatars.get(i).contains(ids.get(i)))
                .forEach(Assert::assertTrue);
    }

    @Test
    public void successRegTest(){
        Spec.installSpecification(Spec.requestSpec(URL), Spec.responseSpecOk200());
        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";
        Register user = new Register("eve.holt@reqres.in", "pistol");
        SuccessReg successReg = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().as(SuccessReg.class);
        Assert.assertEquals(id, successReg.getId());
        Assert.assertEquals(token, successReg.getToken());
    }

    @Test
    public void failedRegTest(){
        Spec.installSpecification(Spec.requestSpec(URL), Spec.responseSpecError400());
        Register user = new Register("sydney@fife", "");
        FailedReg failedReg = given()
                .body(user)
                .post("api/register")
                .then().log().all()
                .extract().as(FailedReg.class);
        Assert.assertEquals(failedReg.getError(), "Missing password");
    }
}
