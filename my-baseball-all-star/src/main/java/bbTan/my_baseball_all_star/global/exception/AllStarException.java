package bbTan.my_baseball_all_star.global.exception;

import org.springframework.http.HttpStatusCode;

public class AllStarException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public AllStarException(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    @Override
    public String getMessage() {
        return exceptionCode.getMessage();
    }

    public HttpStatusCode getHttpStatusCode() {
        return exceptionCode.getHttpStatus();
    }
}

