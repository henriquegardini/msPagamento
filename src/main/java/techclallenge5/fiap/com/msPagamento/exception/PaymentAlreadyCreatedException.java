package techclallenge5.fiap.com.msPagamento.exception;

public class PaymentAlreadyCreatedException extends RuntimeException {
    public PaymentAlreadyCreatedException() {
        super("Payment for this ShoppingCart already created.");
    }
    public PaymentAlreadyCreatedException(String msg){
        super(msg);
    }
}
