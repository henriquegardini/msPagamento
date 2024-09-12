package techclallenge5.fiap.com.msPagamento.exception;

public class PaymentInvalidStatusException extends RuntimeException {
    public PaymentInvalidStatusException() {
        super("Payment for this order already completed.");
    }
    public PaymentInvalidStatusException(String msg){
        super(msg);
    }
}
