package com.chat.exception;

import lombok.SneakyThrows;
import net.minidev.json.JSONObject;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "rawtypes"})
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler implements ErrorController {

    public static Map<String, Object> prepareErrorJSON(final HttpStatus status, final Exception ex) {
        Map respond = new HashMap();
        respond.put("status", status.value());
        respond.put("error", status.getReasonPhrase());
        respond.put("code", ex.getMessage());
        respond.put("time", ZonedDateTime.now(ZoneId.of("Z")));
        if (!(ex instanceof CustomException)) {
            respond.put("code", "0500");
        }
        return respond;
    }

    @SneakyThrows
    public static void getExceptionResponse(HttpServletResponse response, HttpStatus httpStatus) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(String.valueOf(
                new JSONObject().appendField("code", httpStatus)
                        .appendField("status", HttpStatus.FORBIDDEN)));
    }

    @RequestMapping("/error")
    public ResponseEntity handleError(final HttpServletRequest request,
                                      final HttpServletResponse response) {

        Object exception = request.getAttribute("javax.servlet.error.exception");
        //System.out.println(exception);
        // TODO: Logic to inspect exception thrown from Filters...
        return ResponseEntity.badRequest().body(new Error(/* whatever */));
    }

    @ExceptionHandler({CustomException.class})
    public ResponseEntity<Object> applicationException(CustomException exception, WebRequest request) {
        return ResponseEntity.badRequest().body(prepareErrorJSON((HttpStatus) exception.getStatus(), exception));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        //Get all errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        body.put("errors", errors);

        return ResponseEntity.status(status).headers(headers).body(body);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        //Get all errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());
        if (errors.size() == 0)
            errors = getAllErrorsIfFieldErrorsIsEmpty(ex);
        body.put("errors", errors);

        return ResponseEntity.status(status).headers(headers).body(body);
    }

    private List<String> getAllErrorsIfFieldErrorsIsEmpty(BindException ex) {
        return ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());


        body.put("errors", ex.getMessage());

        return ResponseEntity.status(status).headers(headers).body(body);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }
        return ResponseEntity.badRequest().body(prepareErrorJSON(HttpStatus.BAD_REQUEST, ex));
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        String error =
                ex.getName() + " should be of type " + ex.getRequiredType().getName();

        return ResponseEntity.badRequest().body(prepareErrorJSON(HttpStatus.BAD_REQUEST, ex));
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(
                " method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

        return ResponseEntity.badRequest().body(prepareErrorJSON(HttpStatus.BAD_REQUEST, ex));

    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

        return ResponseEntity.badRequest().body(prepareErrorJSON(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        return ResponseEntity.badRequest().body(prepareErrorJSON(HttpStatus.BAD_REQUEST, ex));
    }
}
