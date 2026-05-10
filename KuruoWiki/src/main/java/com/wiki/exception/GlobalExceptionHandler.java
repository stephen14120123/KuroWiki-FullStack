package com.wiki.exception;

import com.wiki.common.Result;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局统一异常处理
 * 确保前端无论遇到什么报错都能拿到标准 JSON 格式的 Result 对象。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 参数校验失败（@Validated + @RequestBody）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("；"));
        return Result.error(message);
    }

    /**
     * 路径参数/请求参数校验失败（@Validated + @PathVariable/@RequestParam）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolation(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("；"));
        return Result.error(message);
    }

    /**
     * 缺少必要请求参数
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<Void> handleMissingParam(MissingServletRequestParameterException e) {
        return Result.error("缺少必要参数：" + e.getParameterName());
    }

    /**
     * 请求体解析失败（JSON 格式错误）
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleMessageNotReadable(HttpMessageNotReadableException e) {
        return Result.error("请求数据格式错误，请检查 JSON 格式");
    }

    /**
     * 请求方法不支持（如 GET 接口用了 POST）
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Void> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        return Result.error("不支持的请求方式：" + e.getMethod());
    }

    /**
     * 文件上传超出大小限制
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result<Void> handleMaxUploadSize(MaxUploadSizeExceededException e) {
        return Result.error("上传文件过大，请压缩后重试");
    }

    /**
     * 业务异常（自定义，如参数不合法、数据不存在等）
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgument(IllegalArgumentException e) {
        return Result.error(e.getMessage());
    }

    /**
     * 空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public Result<Void> handleNullPointer(NullPointerException e) {
        return Result.error("系统内部错误（空指针）");
    }

    /**
     * 兜底：所有未被上面捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        return Result.error("系统异常，请稍后重试：" + e.getMessage());
    }
}
