package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;

public class BaseSpecs {
    public static RequestSpecification reqSpec = with()
            .filter(withCustomTemplates())
            .log().all()
            .contentType("application/json");

    public static ResponseSpecification returnResSpec(int expectedStatusCode){
        return  new ResponseSpecBuilder()
                .expectStatusCode(expectedStatusCode)
                .log(STATUS)
                .log(BODY)
                .build();
    }
}
