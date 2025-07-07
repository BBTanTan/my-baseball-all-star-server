package bbTan.my_baseball_all_star.controller.dto.request;

import bbTan.my_baseball_all_star.domain.TeamRoaster;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record TeamRequest (
        @NotBlank(message = "팀 이름이 존재하지 않습니다.") String teamName,
        @NotEmpty(message = "플레이어 리스트가 비어있습니다.") List<Long> playerIds
) {
}
