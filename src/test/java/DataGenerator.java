import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    @Value
    public static class RegistrationData {
        String login;
        String password;
        String status;
    }

    private static final RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static final Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }

    private static void sendRequest(RegistrationData user) {
        given()
                .spec(requestSpecification)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static String randomLogin() {
        return faker.name().username();
    }

    public static String randomPassword() {
        return faker.internet().password();
    }

    public static class Registration {
        private Registration() {
        }

        public static RegistrationData getUser(String status) {
            RegistrationData user = new RegistrationData(randomLogin(), randomPassword(), status);
            return user;
        }

        public static RegistrationData getRegisteredUser(String status) {
            var registeredUser = getUser(status);
            sendRequest(registeredUser);
            return registeredUser;
        }
    }
}
