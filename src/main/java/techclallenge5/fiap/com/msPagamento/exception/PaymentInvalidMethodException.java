package techclallenge5.fiap.com.msPagamento.exception;

public class PaymentInvalidMethodException extends RuntimeException {
    public PaymentInvalidMethodException() {
        super("Payment method not valid.");
    }
    public PaymentInvalidMethodException(String msg){
        super(msg);
    }
}
