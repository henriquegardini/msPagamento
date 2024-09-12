package techclallenge5.fiap.com.msPagamento.exception;

public class PaymentInvalidMethodTransitionException extends RuntimeException {
    public PaymentInvalidMethodTransitionException() {
        super("Payment method transition invalid. It needs to be different from previous one.");
    }
    public PaymentInvalidMethodTransitionException(String msg){
        super(msg);
    }
}
