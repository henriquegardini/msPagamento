package techclallenge5.fiap.com.msPagamento.dto;

import techclallenge5.fiap.com.msPagamento.model.PaymentMethod;

import java.time.LocalDateTime;

public record PaymentResponseDTO(String orderId,
                                 PaymentMethod paymentMethod,
                                 LocalDateTime paymentStartDate,
                                 LocalDateTime paymentDueDate) {
}
