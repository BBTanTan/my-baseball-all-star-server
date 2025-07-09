package bbTan.my_baseball_all_star.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record FriendPlayCreateRequest(@NotBlank(message = "팀 이름은 필수입니다.") String teamName,
                                      @NotNull(message = "선수 목록은 필수입니다.") List<Long> playerIds,
                                      @NotBlank(message = "비밀번호는 필수입니다.") String password
) {
}
