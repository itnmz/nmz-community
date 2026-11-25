package com.itnmz.community.common.exception;

import com.itnmz.community.common.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 全局异常处理。
 *
 * 所有异常最终都转成统一的 Result 结构，前端只需要认 code 一个字段。
 * 注意：拦截器里 preHandle 返回 false 的响应不经过这里，必须在拦截器内部自己写 JSON。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 业务异常：HTTP 状态码与业务 code 保持一致。
     *
     * 之前统一返回 HTTP 200，导致同样是「未授权」，密码错误是 200、拦截器拦截是 401，
     * 前端按 HTTP 状态判断会漏掉一半分支。现在两者统一走真实状态码：
     * 密码错误 401、账号禁用 403、未注册 404、参数类 400。
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<Void>> handleBusiness(BusinessException e) {
        log.warn("业务异常: code={}, message={}", e.getCode(), e.getMessage());
        HttpStatus status = e.getCode() == null ? null : HttpStatus.resolve(e.getCode());
        // code 不在 HTTP 标准状态码范围内时退回 400，避免 ResponseEntity.status 抛异常
        return ResponseEntity.status(status == null ? HttpStatus.BAD_REQUEST : status)
                .body(Result.fail(e.getCode(), e.getMessage()));
    }

    /**
     * @Valid 校验失败：只取第一条错误信息，别让用户看一堆 FieldError。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Void>> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fe -> fe.getDefaultMessage())
                .orElse("参数校验失败");
        return ResponseEntity.badRequest().body(Result.fail(400, message));
    }

    /**
     * 请求体不是合法 JSON，或字段类型对不上（比如 phone 传了对象）。
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Result<Void>> handleUnreadable(HttpMessageNotReadableException e) {
        log.warn("请求体解析失败: {}", e.getMessage());
        return ResponseEntity.badRequest().body(Result.fail(400, "请求体格式错误"));
    }

    /**
     * 路径不存在。不单独处理会被下面的 Exception 兜底成 500，误导排查方向。
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Result<Void>> handleNotFound(NoResourceFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Result.fail(404, "接口不存在: " + e.getResourcePath()));
    }

    /**
     * 兜底。任何没被上面接住的异常都在这里变成 500，日志里留完整堆栈。
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleOther(Exception e) {
        log.error("未捕获异常", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.fail(500, "服务器内部错误"));
    }
}
