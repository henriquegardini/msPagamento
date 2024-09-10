package techclallenge5.fiap.com.msPagamento.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(PaymentInvalidStatusException.class)
    public ResponseEntity<String> handlePaymentInvalidStatusException(final PaymentInvalidStatusException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<String> handlePaymentNotFoundException(final PaymentNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

}
