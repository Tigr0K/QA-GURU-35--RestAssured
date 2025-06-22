package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import models.lombok.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static specs.CreateNewUserSpec.createUserResponceSpec;
import static specs.CreateNewUserSpec.createUserSpec;
import static specs.DeleteUserSpec.deleteResponseSpec;
import static specs.DeleteUserSpec.deleteSpec;
import static specs.GetUserNegativeSpec.getUserNegativeResponseSpec;
import static specs.GetUserNegativeSpec.getUserNegativeSpec;
import static specs.GetUserSpec.getUserResponseSpec;
import static specs.GetUserSpec.getUserSpec;
import static specs.RegisterSpec.registerResponseSpec;
import static specs.RegisterSpec.registerSpec;

@Tag("SMOKE")
public class ReqresTestsWithModel {

    public int minTokenLength = 16;

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
        Configuration.pageLoadStrategy = "eager";
        Configuration.remote = "https://user1:1234@selenoid.autotests.cloud/wd/hub";

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                "enableVNC", true,
                "enableVideo", true
        ));
        Configuration.browserCapabilities = capabilities;
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @Test
    @DisplayName("Получение одного пользователя позитивный")
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
    @DisplayName("Получение одного пользователя пользователь не найден")
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
    @DisplayName("Создание нового пользователя")
    void createNewUserTest() {
        CreateNewUserResponseLombokModel response = step("Make tequest", () -> {
            CreateNewUserResponseLombokModel createData = new CreateNewUserResponseLombokModel();
            createData.setName("morpheus");
            createData.setJob("leader");
            return given(createUserSpec)
                    .header("x-api-key", "reqres-free-v1")
                    .body(createData)
                    .post("/users")
                    .then()
                    .spec(createUserResponceSpec)
                    .extract().body().as(CreateNewUserResponseLombokModel.class);
        });

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
        step("Make tequest", () -> {
            return given(deleteSpec)
                    .header("x-api-key", "reqres-free-v1")
                    .delete("/users/2")
                    .then()
                    .spec(deleteResponseSpec);
        });

    }

    @Test
    @DisplayName("Регистрация нового пользователя")
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
