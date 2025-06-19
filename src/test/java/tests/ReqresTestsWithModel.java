package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import models.lombok.GetUserResponceLombokModel;
import models.lombok.RegisterBodyLombokModel;
import models.lombok.RegisterResponseLombokBodyModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.DeleteUserSpec.deleteResponseSpec;
import static specs.DeleteUserSpec.deleteSpec;
import static specs.GetUserNegativeSpec.getUserNegativeResponseSpec;
import static specs.GetUserNegativeSpec.getUserNegativeSpec;
import static specs.GetUserSpec.getUserResponseSpec;
import static specs.GetUserSpec.getUserSpec;
import static specs.RegisterSpec.registerResponseSpec;
import static specs.RegisterSpec.registerSpec;

public class ReqresTestsWithModel {

    public int minTokenLength = 16;

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }

    @Test
    void getSingleUserPositiveTest() {
        GetUserResponceLombokModel responce = step("Make tequest", () -> {
            return given(getUserSpec)
                    .header("x-api-key", "reqres-free-v1")
                    .get("/users/2")
                    .then()
                    .spec(getUserResponseSpec)
                    .extract().body().as(GetUserResponceLombokModel.class);
        });

        step("Check responce", () -> {
            assertEquals(2, responce.getData().getId());
            assertEquals("janet.weaver@reqres.in", responce.getData().getEmail());
            assertEquals("Janet", responce.getData().getFirst_name());
            assertEquals("Weaver", responce.getData().getLast_name());
            assertEquals("https://reqres.in/img/faces/2-image.jpg", responce.getData().getAvatar());
        });
    }


    @Test
    void getSingleUserNotFoundTest() {
        GetUserResponceLombokModel responce = step("Make tequest", () -> {
            return given(getUserNegativeSpec)
                    .header("x-api-key", "reqres-free-v1")
                    .get("/users/23")
                    .then()
                    .spec(getUserNegativeResponseSpec)
                    .extract().body().as(GetUserResponceLombokModel.class);
        });

    }

    @Test
    void createNewUserTest() {
        given()
                .log().all()
                .header("x-api-key", "reqres-free-v1")
                .body("{\"name\": \"morpheus\", \"job\": \"leader\"}")
                .contentType(JSON)
                .post("/users")
                .then()
                .log().all()
                .statusCode(201)
                .body("", hasKey("id"))
                .body("", hasKey("createdAt"))
        ;
    }

    @Test
    void deleteUserTest() {
        step("Make tequest", () -> {
            return given(deleteSpec)
                    .header("x-api-key", "reqres-free-v1")
                    .delete("/users/2")
                    .then()
                    .spec(deleteResponseSpec);
        });

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
                    .post("/register")
                    .then()
                    .spec(registerResponseSpec)
                    .extract().body().as(RegisterResponseLombokBodyModel.class);
        });

        step("Check responce", () -> {
            assertEquals("QpwL5tke4Pnpja7X4", responce.getToken());
            assertEquals(4, responce.getId());
        });

    }
}
