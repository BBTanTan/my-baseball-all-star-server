package bbTan.my_baseball_all_star.controller.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record SoloPlayRequest(@Valid @NotNull(message = "홈 팀 정보가 존재하지 않습니다.") TeamRequest homeTeam,
                              @Valid @NotNull(message = "어웨이 팀 정보가 존재하지 않습니다.") TeamRequest awayTeam
) {
}
