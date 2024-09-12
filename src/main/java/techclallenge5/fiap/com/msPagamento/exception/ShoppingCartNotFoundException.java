package techclallenge5.fiap.com.msPagamento.exception;

public class ShoppingCartNotFoundException extends RuntimeException {
    public ShoppingCartNotFoundException() {
        super("Shopping cart not found");
    }
    public ShoppingCartNotFoundException(String msg){
        super(msg);
    }
}
