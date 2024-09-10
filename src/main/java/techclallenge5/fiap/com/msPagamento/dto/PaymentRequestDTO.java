package techclallenge5.fiap.com.msPagamento.dto;

import techclallenge5.fiap.com.msPagamento.model.PaymentMethod;

public record PaymentRequestDTO(String orderId,
                                PaymentMethod paymentMethod) {
}
