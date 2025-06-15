package tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import io.restassured.RestAssured;

public class ReqresTests {

    public int minTokenLength = 16;

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }

    @Test
    void getSingleUserPositiveTest() {
        get("/users/2")
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
    void getSingleUserNotFoundTest() {
        given()
                .header("x-api-key", "reqres-free-v1")
                .get("/api/users/23")
                .then()
                .statusCode(404);
    }

    @Test
    void createNewUserTest() {
        given()
                .log().all()
                .header("x-api-key", "reqres-free-v1")
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"leader\"\n" +
                        "}")
                .contentType(JSON)
                .post("/api/users")
                .then()
                .log().all()
                .statusCode(201)
                .body("", hasKey("id"))
                .body("", hasKey("createdAt"))
        ;
    }

    @Test
    void deleteUserTest() {
        given()
                .header("x-api-key", "reqres-free-v1")
                .delete("/api/users/2")
                .then()
                .statusCode(204);
    }

    @Test
    void registerUserPositiveTest() {
        given()
                .log().all()
                .header("x-api-key", "reqres-free-v1")
                .body("{\"email\": \"eve.holt@reqres.in\",\"password\": \"pistol\"}")
                .contentType("application/json")
                .post("/register")
                .then()
                .log().all()
                .statusCode(200)
                .body("", hasKey("id"))
                .body("", hasKey("token"))
                .body("id", is(4))
                .body("token", notNullValue())
                .body("token", not(blankString()))
                .body("token.length()", greaterThanOrEqualTo(minTokenLength))
                .body("token", matchesPattern("^[a-zA-Z0-9]+$"));
    }
}