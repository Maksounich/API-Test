package ApiTests;

import api.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;

public class ReqresTest {
    private RequestUtils requestUtils;
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
        Spec.installSpecification(Spec.requestSpec(URL), Spec.responseSpecUnique(200));
        Response response = RequestUtils.sendGetRequest("api/users?page=2");
        List<UserData> users = response.jsonPath().getList("data", UserData.class);

        users.forEach(x -> Assert.assertTrue(x.getAvatar().contains(x.getId().toString())));

        Assert.assertTrue(users.stream().allMatch(x -> x.getEmail().endsWith("@reqres.in")));

        List<String> avatars = users.stream().map(UserData::getAvatar).collect(Collectors.toList());
        List<String> ids = users.stream().map(x -> x.getId().toString()).collect(Collectors.toList());

        Assert.assertTrue(IntStream.range(0, avatars.size())
                .mapToObj(i -> avatars.get(i).contains(ids.get(i)))
                .allMatch(Boolean::booleanValue));
    }

    @Test
    public void successRegTest(){
        Spec.installSpecification(Spec.requestSpec(URL), Spec.responseSpecUnique(200));
        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";
        Register user = new Register("eve.holt@reqres.in", "pistol");
        SuccessReg successReg = (SuccessReg) RequestUtils.sendPostRequest("api/register", user, SuccessReg.class);

        Assert.assertEquals(id, successReg.getId());
        Assert.assertEquals(token, successReg.getToken());
    }

    @Test
    public void failedRegTest(){
        Spec.installSpecification(Spec.requestSpec(URL), Spec.responseSpecUnique(400));
        Register user = new Register("eve.holt@reqres.in", "");
        FailedReg failedReg = (FailedReg) RequestUtils.sendPostRequest("api/register", user, FailedReg.class);

        Assert.assertEquals(failedReg.getError(), "Missing password");
    }

    @Test
    public void sortYearsTest(){
        Spec.installSpecification(Spec.requestSpec(URL), Spec.responseSpecUnique(200));
        Response response = RequestUtils.sendGetRequest("api/unknown");
        List<Resouce> colors = response.jsonPath().getList("data", Resouce.class);
        List<Integer> years = colors.stream().map(Resouce::getYear).collect(Collectors.toList());
        List<Integer> sortedYears = years.stream().sorted().collect(Collectors.toList());

        Assert.assertEquals(years, sortedYears);
    }

    @Test
    public void deleteTest(){
        Spec.installSpecification(Spec.requestSpec(URL), Spec.responseSpecUnique(204));
        Response response = RequestUtils.sendDeleteRequest("api/users/2");

        Assert.assertEquals(204, response.getStatusCode());
    }
}
