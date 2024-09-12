package techclallenge5.fiap.com.msPagamento.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.Instant;

@ControllerAdvice
public class ExceptionHandler {

    private StandardError error = new StandardError();

    @org.springframework.web.bind.annotation.ExceptionHandler(PaymentInvalidStatusException.class)
    public ResponseEntity<StandardError> handlePaymentInvalidStatusException(final PaymentInvalidStatusException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return getStandardErrorResponseEntity(request, status, ex.getMessage(), ex);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<StandardError> handlePaymentNotFoundException(final PaymentNotFoundException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return getStandardErrorResponseEntity(request, status, ex.getMessage(), ex);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<StandardError> handleShoppingCartNotFoundException(final ShoppingCartNotFoundException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return getStandardErrorResponseEntity(request, status, ex.getMessage(), ex);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<StandardError> handlePaymentExpiredException(final PaymentExpiredException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return getStandardErrorResponseEntity(request, status, ex.getMessage(), ex);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<StandardError> handlePaymentAlreadyCreatedException(final PaymentAlreadyCreatedException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return getStandardErrorResponseEntity(request, status, ex.getMessage(), ex);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<StandardError> handlePaymentInvalidMethodException(final PaymentInvalidMethodException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return getStandardErrorResponseEntity(request, status, ex.getMessage(), ex);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<StandardError> handlePaymentInvalidMethodTransitionException(final PaymentInvalidMethodTransitionException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return getStandardErrorResponseEntity(request, status, ex.getMessage(), ex);
    }


    public ResponseEntity<StandardError> validationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ValidateError validateError = new ValidateError();

        validateError.setTimestamp(Instant.now());
        validateError.setStatus(status.value());
        validateError.setError("Validation error.");
        validateError.setMessage(e.getMessage());
        validateError.setPath(request.getRequestURI());

        for (FieldError f : e.getBindingResult().getFieldErrors()) {
            validateError.addMessages(f.getField(), f.getDefaultMessage());
        }
        ;

        return ResponseEntity.status(status).body(validateError);
    }


    private ResponseEntity<StandardError> getStandardErrorResponseEntity(HttpServletRequest request, HttpStatus status, String message, Exception e) {
        error.setTimestamp(Instant.now());
        error.setStatus(status.value());
        error.setError(message);
        error.setMessage(message);
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(this.error);
    }

}
