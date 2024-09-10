package techclallenge5.fiap.com.msPagamento.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;
import techclallenge5.fiap.com.msPagamento.model.ShoppingCart;

@FeignClient(name = "Cart", url = "http://localhost:8082")
public interface ShoppingCartClient {

    @GetMapping("/cart/{orderId}")
    Mono<ShoppingCart> getCartDetails(@PathVariable("orderId") String orderId);

    @PostMapping("/cart/{userId}/update-status")
    Mono<ShoppingCart> updateCartStatus(@PathVariable("userId") Long userId);
}
