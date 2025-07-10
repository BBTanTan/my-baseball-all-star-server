package bbTan.my_baseball_all_star.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {

    // 전체
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 서버에러가 발생했습니다"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 인자입니다."),
    INVALID_REQUEST_PATH(HttpStatus.BAD_REQUEST, "유효하지 않은 경로 요청입니다."),

    // PlayMode
    INVALID_PLAY_MODE(HttpStatus.BAD_REQUEST, "잘못된 경기 모드입니다."),

    // PlayerChoiceCount
    PLAYER_CHOICE_COUNT_NOT_FOUND(HttpStatus.BAD_REQUEST, "선수 ID가 존재하지 않습니다."),

    // TeamRoaster
    INVALID_TEAM_PLAYERS_SIZE(HttpStatus.BAD_REQUEST, "한 팀당 선수는 12명이어야 합니다."),
    MISSING_POSITION_IN_TEAM_ROSTER(HttpStatus.BAD_REQUEST, "팀 명단에 모든 포지션이 포함되어야 합니다."),
    INVALID_CHOICE_COUNT_SIZE(HttpStatus.BAD_REQUEST, "선수 점수와 선택 횟수의 개수가 일치하지 않습니다."),
    DUPLICATE_PLAYERS_IN_TEAM_ROSTER(HttpStatus.BAD_REQUEST, "중복된 선수가 존재합니다."),

    // Team
    TEAM_NOT_FOUND(HttpStatus.BAD_REQUEST, "팀을 찾을 수 없습니다."),

    // PlayResult
    INVALID_RESULT_SCORE_SIZE(HttpStatus.BAD_REQUEST, "경기 결과는 반드시 두 팀에 대한 점수여야 합니다."),

    // PlayerScore
    INVALID_PLAYER_SCORE(HttpStatus.BAD_REQUEST, "유효하지 않은 점수 결과입니다"),

    // PlayShare
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호는 4자 이상 10자 이하여야 합니다."),
    TEAM_URL_NOT_FOUND(HttpStatus.BAD_REQUEST, "URL에 해당하는 팀을 찾을 수 없습니다."),
    TEAM_SHARE_NOT_FOUND(HttpStatus.BAD_REQUEST, "팀에 해당하는 공유 현황을 찾을 수 없습니다."),
    INVALID_PLAY_SHARE_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    //Player
    NO_PLAYER(HttpStatus.BAD_REQUEST, "선수 정보가 존재하지 않습니다."),
    INSUFFICIENT_CANDIDATES(HttpStatus.UNPROCESSABLE_ENTITY, "후보 선수가 부족합니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    ExceptionCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
       this.message = message;
    }
}
