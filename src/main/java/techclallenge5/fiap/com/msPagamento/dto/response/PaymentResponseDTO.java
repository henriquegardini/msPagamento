package techclallenge5.fiap.com.msPagamento.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import techclallenge5.fiap.com.msPagamento.model.PaymentMethod;
import techclallenge5.fiap.com.msPagamento.model.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentResponseDTO(String id,
                                 String shoppingCartId,
                                 Double value,
                                 PaymentMethod paymentMethod,
                                 PaymentStatus status,
                                 @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime paymentStartDate,
                                 @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime paymentDueDate) {
}
