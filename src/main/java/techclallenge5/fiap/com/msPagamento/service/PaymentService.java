package techclallenge5.fiap.com.msPagamento.service;

import techclallenge5.fiap.com.msPagamento.dto.response.PaymentPixResponseDTO;
import techclallenge5.fiap.com.msPagamento.dto.request.PaymentRequestDTO;
import techclallenge5.fiap.com.msPagamento.dto.response.PaymentResponseDTO;
import techclallenge5.fiap.com.msPagamento.dto.request.PaymentUpdateMethodRequestDTO;

public interface PaymentService {
    PaymentResponseDTO createPaymentOrder(PaymentRequestDTO paymentRequestDTO);
    PaymentPixResponseDTO createPixPaymentOrder(PaymentRequestDTO paymentRequestDTO);
    PaymentResponseDTO updatePaymentMethod(PaymentUpdateMethodRequestDTO paymentRequestDTO, String id);
    void deletePayment(String id);
    PaymentResponseDTO processPayment(String id, String token);
}
