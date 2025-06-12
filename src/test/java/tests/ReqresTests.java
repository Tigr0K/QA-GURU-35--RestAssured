package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import models.lombok.RegisterBodyLombokModel;
import models.lombok.RegisterResponseLombokBodyModel;
import models.pojo.RegisterBodyModel;
import models.pojo.RegisterResponseBodyModel;
import org.junit.jupiter.api.Test;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.RegisterSpec.registerResponseSpec;
import static specs.RegisterSpec.registerSpec;

public class ReqresTests {

    @Test
    void getSingleUserPositive() {
        get("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("data.id", is(2))
                .body("data.email", is("janet.weaver@reqres.in"))
                .body("data.first_name", is("Janet"))
                .body("data.last_name", is("Weaver"))
                .body("data.avatar", is("https://reqres.in/img/faces/2-image.jpg"))
        ;
    }

    @Test
    void getSingleUserNotFound() {
        given()
                .header("x-api-key", "reqres-free-v1")
                .get("https://reqres.in/api/users/23")
                .then()
                .statusCode(404);
    }

    @Test
    void createNewUser() {
        given()
                .log().all()
                .header("x-api-key", "reqres-free-v1")
                .body("{\"name\": \"morpheus\", \"job\": \"leader\"}")
                .contentType(JSON)
                .post("https://reqres.in/api/users")
                .then()
                .log().all()
                .statusCode(201)
                .body("", hasKey("id"))
                .body("", hasKey("createdAt"))
        ;
    }

    @Test
    void deleteUser() {
        given()
                .header("x-api-key", "reqres-free-v1")
                .delete("https://reqres.in/api/users/2")
                .then()
                .statusCode(204);
    }

    @Test
    void registerUserPOJOPositive() {
        RegisterBodyModel authData = new RegisterBodyModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("pistol");

        RegisterResponseBodyModel responce = given()
                .log().all()
                .header("x-api-key", "reqres-free-v1")
                .body(authData)
                .contentType("application/json")
                .post("https://reqres.in/api/register")
                .then()
                .log().all()
                .statusCode(200)
                .extract().body().as(RegisterResponseBodyModel.class);

        assertEquals("QpwL5tke4Pnpja7X4", responce.getToken());
        assertEquals(4, responce.getId());
    }

    @Test
    void registerUserLombokPositive() {
        RegisterBodyLombokModel authData = new RegisterBodyLombokModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("pistol");

        RegisterResponseLombokBodyModel responce = given()
                .filter(new AllureRestAssured())
                .log().all()
                .header("x-api-key", "reqres-free-v1")
                .body(authData)
                .contentType("application/json")
                .post("https://reqres.in/api/register")
                .then()
                .log().all()
                .statusCode(200)
                .extract().body().as(RegisterResponseLombokBodyModel.class);

        assertEquals("QpwL5tke4Pnpja7X4", responce.getToken());
        assertEquals(4, responce.getId());
    }

    @Test
    void registerUserLombokPositiveCastomAllure() {
        RegisterBodyLombokModel authData = new RegisterBodyLombokModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("pistol");
        RegisterResponseLombokBodyModel responce = step("Make request", () -> {
            return given(registerSpec)
                    .header("x-api-key", "reqres-free-v1")
                    .body(authData)
                    .post("/api/register")
                    .then()
                    .spec(registerResponseSpec)
                    .statusCode(200)
                    .extract().body().as(RegisterResponseLombokBodyModel.class);
        });

        step("Check responce", () -> {
            assertEquals("QpwL5tke4Pnpja7X4", responce.getToken());
            assertEquals(4, responce.getId());
        });

    }
}
