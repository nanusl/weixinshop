package com.github.exception;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @author nanusl
 * @version V1.0
 * @Description 全局异常处理
 * @date 2021-01-21 18:53
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public HttpEntity<String> defaultErrorHandler(HttpServletRequest request, Exception e) {
        logger.error("服务器出现未处理错误: url : " + request.getRequestURL(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("服务器出现未处理错误: %s", e.getMessage()));
    }

    /**
     * 通用的接口映射异常处理方
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseEntity<Object> result;
        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) ex;
            result = ResponseEntity.status(status).body(exception.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        } else if (ex instanceof MethodArgumentTypeMismatchException) {
            MethodArgumentTypeMismatchException exception = (MethodArgumentTypeMismatchException) ex;
            logger.error("参数转换失败，方法：" + exception.getParameter().getMethod().getName() + "，参数：" + exception.getName()
                    + ",信息：" + exception.getLocalizedMessage());
            result = ResponseEntity.status(status).body("参数转换失败");
        } else {
            result = ResponseEntity.status(status).body(String.format("服务器出现未处理错误: %s", ex.getMessage()));
        }

        return result;
    }
}
