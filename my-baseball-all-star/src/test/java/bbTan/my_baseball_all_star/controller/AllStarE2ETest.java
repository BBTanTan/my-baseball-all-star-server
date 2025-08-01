package bbTan.my_baseball_all_star.controller;

import bbTan.my_baseball_all_star.AcceptanceTest;
import bbTan.my_baseball_all_star.controller.dto.request.TeamPlayResultRequest;
import bbTan.my_baseball_all_star.fixture.AllStarRequestFixture;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

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

    @DisplayName("친구 경기 성공")
    @Test
    void friendPlay() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(AllStarRequestFixture.FRIEND_PLAY_REQUEST())
                .when().post("/plays/friend")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("친구 경기 실패: homeTeamId가 null일 경우")
    @Test
    void friendPlay_homeTeamIdNull_exception() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(AllStarRequestFixture.FRIEND_PLAY_REQUEST_HOME_ID_NULL())
                .when().post("/plays/friend")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("친구 경기 실패: awayTeam의 팀 이름이 비었을 경우")
    @Test
    void friendPlay_awayTeamNameEmpty_exception() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(AllStarRequestFixture.FRIEND_PLAY_REQUEST_AWAY_TEAM_NAME_EMPTY())
                .when().post("/plays/friend")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("친구 경기 실패: awayTeam 선수 목록이 비었을 경우")
    @Test
    void friendPlay_awayPlayersEmpty_exception() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(AllStarRequestFixture.FRIEND_PLAY_REQUEST_AWAY_PLAYERS_EMPTY())
                .when().post("/plays/friend")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("친구 초대용 팀 생성 성공")
    @Test
    void createFriendPlay() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(AllStarRequestFixture.FRIEND_PLAY_CREATE_REQUEST())
                .when().post("/teams")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("친구 초대용 팀 생성 실패: 팀 이름이 null일 경우")
    @Test
    void createFriendPlay_teamNameNull_exception() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(AllStarRequestFixture.FRIEND_PLAY_CREATE_REQUEST_TEAM_NAME_NULL())
                .when().post("/teams")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("친구 초대용 팀 생성 실패: 선수 목록이 비어 있을 경우")
    @Test
    void createFriendPlay_playersEmpty_exception() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(AllStarRequestFixture.FRIEND_PLAY_CREATE_REQUEST_PLAYERS_EMPTY())
                .when().post("/teams")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("친구 초대용 팀 생성 실패: 비밀번호가 비어 있을 경우")
    @Test
    void createFriendPlay_passwordEmpty_exception() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(AllStarRequestFixture.FRIEND_PLAY_CREATE_REQUEST_PASSWORD_EMPTY())
                .when().post("/teams")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("친구 초대용 팀 조회 성공")
    @Test
    void readFriendPlayTeam() {
        // given
        String teamUuid = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(AllStarRequestFixture.FRIEND_PLAY_CREATE_REQUEST())
                .when().post("/teams")
                .then().log().all()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getString("teamUuid");

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/teams/{team-uuid}", teamUuid)
                .then().log().all()
                .statusCode(200)
                .body("teamId", notNullValue())
                .body("teamName", notNullValue());
    }

    @DisplayName("팀 경기 결과 조회 성공")
    @Test
    void readTeamPlayResults() {
        TeamPlayResultRequest request = new TeamPlayResultRequest("pw1234");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/play-results/1")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("팀 경기 결과 조회 실패 : 비밀번호가 없을 경우")
    @Test
    void readTeamPlayResults_nullPassword_exception() {
        TeamPlayResultRequest request = new TeamPlayResultRequest(null);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/play-results/1")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("랜덤 팀 생성 성공")
    @Test
    void getRandomTeams() {
        RestAssured
                .given().log().all()
                .queryParam("mode", "random")
                .when()
                .get("/teams")
                .then().log().all()
                .statusCode(200)
                .body("playerResponses.size()", equalTo(12))
                .extract()
                .jsonPath();
    }

    @DisplayName("랜덤 팀 생성 실패 : 잘못된 mode 값")
    @Test
    void getRandomTeams_wrong_mode_exception() {
        RestAssured
                .given().log().all()
                .queryParam("mode", "wrong")
                .when()
                .get("/teams")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("랜덤 팀 생성 실패 : 파라미터 누락")
    @Test
    void getRandomTeams_missing_parameter_exception() {
        RestAssured
                .given().log().all()
                .when()
                .get("/teams")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("선수 전체 목록 반환 성공")
    @Test
    void getAllPlayersByPosition() {
        ValidatableResponse response = RestAssured
                .given().log().all()
                .when()
                .get("/players")
                .then().log().all()
                .statusCode(200);

        response
                .body("size()", equalTo(10));

        response
                .body("[0].players.size()", greaterThanOrEqualTo(1));
    }
}
