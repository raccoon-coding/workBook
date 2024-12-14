package science.workbook.config.log;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;
import science.workbook.dto.toService.CreateLogDto;
import science.workbook.service.LogService;

import java.util.Objects;
import java.util.function.Consumer;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {
    private final LogService logService;

    @Around("science.workbook.config.log.LogPointCut.viewController()")
    public Object beforeAdviceNotTokenLog(ProceedingJoinPoint joinPoint) throws Throwable {
        return tryLog(joinPoint, this::notTokenLogging);
    }

    private Object tryLog(ProceedingJoinPoint joinPoint, Consumer<ProceedingJoinPoint> logMethod) throws Throwable {
        Object result = joinPoint.proceed();
        logMethod.accept(joinPoint);
        return result;
    }

    private void notTokenLogging(ProceedingJoinPoint joinPoint) {
        String requestBody = getRequestBody();
        viewLog(joinPoint, requestBody);
        createSuccessLog(joinPoint, requestBody);
    }

    private String getRequestBody() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest();
        return new String(((ContentCachingRequestWrapper) request).getContentAsByteArray());
    }

    private void createSuccessLog(ProceedingJoinPoint thisJoinPoint, String requestBody) {
        CreateLogDto dto = CreateLogDto.ofSuccessLog(thisJoinPoint.getSignature().getName(), requestBody);
        logService.createLog(dto);
    }

    private void createExceptionLog(ProceedingJoinPoint thisJoinPoint, String requestBody, String exceptionMessage) {
        CreateLogDto dto = CreateLogDto.ofExceptionLog(thisJoinPoint.getSignature().getName(), requestBody, exceptionMessage);
        logService.createLog(dto);
    }

    private void viewLog(ProceedingJoinPoint thisJoinPoint, String requestBody) {
        log.info("user Request Data : {}", requestBody);
        log.info("log signature : {}", thisJoinPoint.getSignature().getName());
    }
}
