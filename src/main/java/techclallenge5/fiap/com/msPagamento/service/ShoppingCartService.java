//package techclallenge5.fiap.com.msPagamento.service;
//
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//import techclallenge5.fiap.com.msPagamento.model.ShoppingCart;
//
//@Service
//public class ShoppingCartService {
//    private final WebClient webClient;
//
//    public ShoppingCartService(WebClient.Builder webClientBuilder) {
//        this.webClient = webClientBuilder.baseUrl("http://carrinho-service").build();
//    }
//
//    public Mono<ShoppingCart> getCartDetails(String orderId) {
//        return webClient.get()
//                .uri(uriBuilder -> uriBuilder.path("/cart/{orderId}").build(orderId))
//                .retrieve()
//                .bodyToMono(ShoppingCart.class);
//    }
//
//    public Mono<ShoppingCart> updateCartStatus(Long userId) {
//        return webClient.post()
//                .uri(uriBuilder -> uriBuilder.path("/cart/{userId}/update-status").build(userId))
//                .retrieve()
//                .bodyToMono(ShoppingCart.class);
//    }
//}
