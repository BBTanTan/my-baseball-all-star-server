package bbTan.my_baseball_all_star.controller.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record FriendPlayRequest(@NotNull(message = "홈 팀 정보가 존재하지 않습니다.") Long homeTeamId,
                                @Valid @NotNull(message = "어웨이 팀 정보가 존재하지 않습니다.") TeamRequest awayTeam
) {
}
