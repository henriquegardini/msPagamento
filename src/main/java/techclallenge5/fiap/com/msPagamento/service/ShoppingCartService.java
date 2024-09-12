package techclallenge5.fiap.com.msPagamento.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;
import techclallenge5.fiap.com.msPagamento.model.ShoppingCart;

@Service
public class ShoppingCartService {
    private final WebClient webClient;

    public ShoppingCartService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8083/cart").build();
    }

    public ShoppingCart getCartDetails(String orderId) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/{orderId}").build(orderId))
                    .retrieve()
                    .bodyToFlux(ShoppingCart.class)
                    .next()
                    .block();
        } catch (WebClientException e) {
            throw e;
        }
    }

    public ShoppingCart updateCartStatus(Long userId, String authToken) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/{userId}/update-status")
                        .queryParam("authToken", authToken)
                        .build(userId))
                .retrieve()
                .bodyToFlux(ShoppingCart.class).blockFirst();
    }
}
