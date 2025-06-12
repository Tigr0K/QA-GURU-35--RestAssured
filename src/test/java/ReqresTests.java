import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

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
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"leader\"\n" +
                        "}")
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
    void registerUserPositive() {
        given()
                .log().all()
                .header("x-api-key", "reqres-free-v1")
                .body("{\"email\": \"eve.holt@reqres.in\",\"password\": \"pistol\"}")
                .contentType("application/json")
                .post("https://reqres.in/api/register")
                .then()
                .log().all()
                .statusCode(200)
                .body("", hasKey("id"))
                .body("", hasKey("token"))
                .body("id", is(4))
                .body("token", notNullValue())
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }
}
