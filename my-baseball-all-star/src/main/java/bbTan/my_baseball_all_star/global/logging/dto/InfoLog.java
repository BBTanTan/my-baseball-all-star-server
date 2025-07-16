package bbTan.my_baseball_all_star.global.logging.dto;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.UUID;

public class InfoLog extends BaseLog {

    public InfoLog(LocalDateTime requestTime, String requestUrl, String uuid) {
        super(requestTime, requestUrl, uuid);
    }

    public static InfoLog of(HttpServletRequest request) {
        return new InfoLog(
                LocalDateTime.now(),
                request.getMethod() + ' ' + request.getRequestURI(),
                UUID.randomUUID().toString()
        );
    }
}
