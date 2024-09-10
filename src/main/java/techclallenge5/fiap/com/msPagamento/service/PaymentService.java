package techclallenge5.fiap.com.msPagamento.service;

import techclallenge5.fiap.com.msPagamento.dto.PaymentRequestDTO;
import techclallenge5.fiap.com.msPagamento.dto.PaymentResponseDTO;

public interface PaymentService {
    PaymentResponseDTO createPaymentOrder(PaymentRequestDTO paymentRequestDTO);
    PaymentResponseDTO createPixPaymentOrder(PaymentRequestDTO paymentRequestDTO);
    PaymentResponseDTO updatePaymentMethod(PaymentRequestDTO paymentRequestDTO);
    void deletePayment(String id);
    PaymentResponseDTO processPayment(String id);
}
