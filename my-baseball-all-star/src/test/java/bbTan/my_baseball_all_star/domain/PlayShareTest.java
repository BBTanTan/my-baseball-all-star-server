package bbTan.my_baseball_all_star.domain;

import bbTan.my_baseball_all_star.fixture.TeamFixture;
import bbTan.my_baseball_all_star.global.exception.AllStarException;
import bbTan.my_baseball_all_star.global.exception.ExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class PlayShareTest {

    @DisplayName("PlayShare 생성 성공")
    @Test
    void playShareConstructor() {
        // given
        Team team = TeamFixture.TEAM1();
        String password = "123456";

        // when
        PlayShare playShare = new PlayShare(team, password);

        // then
        assertAll(
                () -> assertThat(playShare.getTeam()).isEqualTo(team),
                () -> assertThat(playShare.getPassword()).isEqualTo(password),
                () -> assertThat(playShare.getUrl()).isNotNull()
        );
    }

    @DisplayName("PlayShare 생성 실패 : 비밀번호 null인 경우")
    @Test
    void playShareConstructor_nullPassword_exception() {
        // given
        Team team = TeamFixture.TEAM1();
        String password = null;

        // when & then
        assertThatThrownBy(() -> new PlayShare(team, password))
                .isInstanceOf(AllStarException.class)
                .hasMessageContaining(ExceptionCode.INVALID_PASSWORD.getMessage());
    }

    @DisplayName("PlayShare 생성 실패 : 비밀번호 길이 부족인 경우")
    @Test
    void playShareConstructor_shortPassword_exception() {
        // given
        Team team = TeamFixture.TEAM1();
        String password = "123";

        // when & then
        assertThatThrownBy(() -> new PlayShare(team, password))
                .isInstanceOf(AllStarException.class)
                .hasMessageContaining(ExceptionCode.INVALID_PASSWORD.getMessage());
    }

    @DisplayName("PlayShare 생성 실패 : 비밀번호 길이 초과인 경우")
    @Test
    void playShareConstructor_longPassword_exception() {
        // given
        Team team = TeamFixture.TEAM1();
        String password = "12345678901"; // 11자

        // when & then
        assertThatThrownBy(() -> new PlayShare(team, password))
                .isInstanceOf(AllStarException.class)
                .hasMessageContaining(ExceptionCode.INVALID_PASSWORD.getMessage());
    }

}
