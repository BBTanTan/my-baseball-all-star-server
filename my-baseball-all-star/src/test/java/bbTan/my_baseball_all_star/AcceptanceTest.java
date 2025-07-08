package bbTan.my_baseball_all_star;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/schema-test.sql", "/data-test.sql"})
public abstract class AcceptanceTest extends IntegrationTestSupport {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        setPort();
    }

    private void setPort() {
        RestAssured.port = port;
    }
}
