package bbTan.my_baseball_all_star.controller;

import bbTan.my_baseball_all_star.AcceptanceTest;
import bbTan.my_baseball_all_star.fixture.AllStarRequestFixture;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AllStarE2ETest extends AcceptanceTest {

    @DisplayName("홀로 경기 성공")
    @Test
    void soloPlay() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(AllStarRequestFixture.SOLO_PLAY_REQUEST())
                .when().post("/plays/solo")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("홀로 경기 실패 : Team이 null일 경우")
    @Test
    void soloPlay_teamNull_exception() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(AllStarRequestFixture.SOLO_PLAY_REQUEST_TEAM_NULL())
                .when().post("/plays/solo")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("홀로 경기 실패 : 팀 이름이 비었을 경우")
    @Test
    void soloPlay_teamNameEmpty_exception() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(AllStarRequestFixture.SOLO_PLAY_REQUEST_TEAM_NAME_EMPTY())
                .when().post("/plays/solo")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("홀로 경기 실패 : 선수 목록이 비었을 경우")
    @Test
    void soloPlay_playersEmpty_exception() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(AllStarRequestFixture.SOLO_PLAY_REQUEST_PLAYERS_EMPTY())
                .when().post("/plays/solo")
                .then().log().all()
                .statusCode(400);
    }
}
