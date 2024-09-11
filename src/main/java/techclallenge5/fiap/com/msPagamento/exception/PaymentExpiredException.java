package techclallenge5.fiap.com.msPagamento.exception;

public class PaymentExpiredException extends RuntimeException {
    public PaymentExpiredException() {
        super("Due date of payment expired.");
    }
    public PaymentExpiredException(String msg){
        super(msg);
    }
}
