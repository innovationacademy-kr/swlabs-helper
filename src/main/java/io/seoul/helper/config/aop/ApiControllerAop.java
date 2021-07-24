package io.seoul.helper.config.aop;

import io.seoul.helper.controller.dto.ResultResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;

@Aspect
@Component
@Slf4j
public class ApiControllerAop {
    @Around("@annotation(ApiControllerTryCatch)")
    public Object apiTryCatch(ProceedingJoinPoint joinPoint) {
        try {
            return joinPoint.proceed();
        } catch (EntityNotFoundException e) {
            log.error(joinPoint.getSignature().getName() + "fail : " + e.getMessage());
            return ResultResponseDto.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        } catch (Exception e) {
            log.error(joinPoint.getSignature().getName() + "fail : " + e.getMessage());
            return ResultResponseDto.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        } catch (Throwable e) {
            log.error(joinPoint.getSignature().getName() + "fail : " + e.getMessage());
            return ResultResponseDto.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }
}
