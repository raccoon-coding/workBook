package science.workbook.config.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogPointCut {
    @Pointcut("execution(* science.workbook.controller.viewController.*(..))")
    public void viewController(){
    }
}
