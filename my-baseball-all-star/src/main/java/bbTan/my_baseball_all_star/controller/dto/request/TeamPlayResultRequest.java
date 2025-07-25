package bbTan.my_baseball_all_star.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record TeamPlayResultRequest(
        @NotBlank(message = "비밀번호가 존재하지 않습니다.") String password
) {
}
