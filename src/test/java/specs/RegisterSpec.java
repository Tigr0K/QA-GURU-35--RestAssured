package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.*;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;

public class RegisterSpec {
    public static RequestSpecification registerSpec = with()
            .filter(withCustomTemplates())
            .log().all()
            .contentType("application/json")
            .baseUri("https://reqres.in")
            ;

    public static ResponseSpecification registerResponseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .log(STATUS)
            .log(BODY)
            .build();
}
