package tests;

import io.restassured.RestAssured;
import models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import specs.BaseSpecs;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@Tag("SMOKE")
public class ReqresTestsWithModel {

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }

    @Test
    @DisplayName("Получение одного пользователя позитивный")
    void getSingleUserPositiveTest() {
        GetUserResponseLombokModel responce = step("Make tequest", () -> given(BaseSpecs.reqSpec)
                .header("x-api-key", "reqres-free-v1")
                .get("/users/2")
                .then()
                .spec(BaseSpecs.returnResSpec(200))
                .extract().body().as(GetUserResponseLombokModel.class));

        step("Check responce", () -> {
            assertEquals(2, responce.getData().getId());
            assertEquals("janet.weaver@reqres.in", responce.getData().getEmail());
            assertEquals("Janet", responce.getData().getFirst_name());
            assertEquals("Weaver", responce.getData().getLast_name());
            assertEquals("https://reqres.in/img/faces/2-image.jpg", responce.getData().getAvatar());
        });
    }

    @Test
    @DisplayName("Получение одного пользователя пользователь не найден")
    void getSingleUserNotFoundTest() {
        step("Make tequest", () -> given(BaseSpecs.reqSpec)
                .header("x-api-key", "reqres-free-v1")
                .get("/users/23")
                .then()
                .spec(BaseSpecs.returnResSpec(404))
                .extract().body().as(GetUserResponseLombokModel.class));

    }

    @Test
    @DisplayName("Создание нового пользователя")
    void createNewUserTest() {
        CreateNewUserRequestLombokModel createData = new CreateNewUserRequestLombokModel("morpheus","leader");

        CreateNewUserResponseLombokModel response = step("Make tequest", () -> given(BaseSpecs.reqSpec)
                .header("x-api-key", "reqres-free-v1")
                .body(createData)
                .post("/users")
                .then()
                .spec(BaseSpecs.returnResSpec(201))
                .extract().body().as(CreateNewUserResponseLombokModel.class));

        step("Check responce", () -> {
            assertEquals("morpheus", response.getName());
            assertEquals("leader", response.getJob());
            assertNotNull(response.getId());
            assertNotNull(response.getCreatedAt());
        });
    }

    @Test
    @DisplayName("Удаление пользователя")
    void deleteUserTest() {
        step("Make tequest", () -> given(BaseSpecs.reqSpec)
                .header("x-api-key", "reqres-free-v1")
                .delete("/users/2")
                .then()
                .spec(BaseSpecs.returnResSpec(204)));

    }

    @Test
    @DisplayName("Регистрация нового пользователя")
    void registerUserLombokPositiveCastomAllure() {
        RegisterBodyLombokModel authData = new RegisterBodyLombokModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("pistol");
        RegisterResponseLombokBodyModel responce = step("Make request", () -> given(BaseSpecs.reqSpec)
                .header("x-api-key", "reqres-free-v1")
                .body(authData)
                .post("/register")
                .then()
                .spec(BaseSpecs.returnResSpec(200))
                .extract().body().as(RegisterResponseLombokBodyModel.class));

        step("Check responce", () -> {
            assertEquals("QpwL5tke4Pnpja7X4", responce.getToken());
            assertEquals(4, responce.getId());
        });

    }
}
