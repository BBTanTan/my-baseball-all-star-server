package bbTan.my_baseball_all_star.controller.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record SoloPlayRequest(@NotEmpty(message = "홈 팀 정보가 존재하지 않습니다.") TeamRequest homeTeam,
                              @NotEmpty(message = "어웨이 팀 정보가 존재하지 않습니다.") TeamRequest awayTeam
) {
}
