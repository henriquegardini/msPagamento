package techclallenge5.fiap.com.msPagamento.dto.request;

import techclallenge5.fiap.com.msPagamento.model.PaymentMethod;

public record PaymentRequestDTO(String orderId,
                                PaymentMethod paymentMethod) {
}
