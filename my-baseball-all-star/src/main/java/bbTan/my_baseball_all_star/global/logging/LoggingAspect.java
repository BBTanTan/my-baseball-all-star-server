package bbTan.my_baseball_all_star.global.logging;

import bbTan.my_baseball_all_star.global.logging.dto.ErrorLog;
import bbTan.my_baseball_all_star.global.logging.dto.InfoLog;
import bbTan.my_baseball_all_star.global.logging.dto.WarnLog;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.util.Arrays;
import java.util.Optional;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @AfterReturning("execution(public * bbTan.my_baseball_all_star.controller..*(..))")
    public void loggingInfo(JoinPoint joinPoint) {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            log.info(InfoLog.of(request).toString());
        }
    }

    @Before("execution(* bbTan.my_baseball_all_star.global.exception.GlobalExceptionHandler.*(..)) &&"
            + "!execution(* bbTan.my_baseball_all_star.global.exception.GlobalExceptionHandler.handleRuntimeException(..)))")
    public void loggingWarn(JoinPoint joinPoint) {
        Optional<Exception> exceptionOptional = Arrays.stream(joinPoint.getArgs())
                .filter(arg -> arg instanceof Exception)
                .map(arg -> (Exception) arg)
                .findFirst();

        if (exceptionOptional.isPresent() && RequestContextHolder.getRequestAttributes() != null) {
            Exception exception = exceptionOptional.get();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            log.warn(WarnLog.of(exception, request).toString());
        }
    }

    @Before("execution(* bbTan.my_baseball_all_star.global.exception.GlobalExceptionHandler.handleRuntimeException(..)))")
    public void loggingError(JoinPoint joinPoint) {
        Optional<Exception> exceptionOptional = Arrays.stream(joinPoint.getArgs())
                .filter(arg -> arg instanceof Exception)
                .map(arg -> (Exception) arg)
                .findFirst();

        if (exceptionOptional.isPresent() && RequestContextHolder.getRequestAttributes() != null) {
            Exception exception = exceptionOptional.get();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            log.error(ErrorLog.of(exception, request).toString());
        }
    }
}
