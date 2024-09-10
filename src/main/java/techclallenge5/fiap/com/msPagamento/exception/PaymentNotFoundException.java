package techclallenge5.fiap.com.msPagamento.exception;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException() {
        super("Payment not found");
    }
    public PaymentNotFoundException(String msg){
        super(msg);
    }
}
