package techclallenge5.fiap.com.msPagamento.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import techclallenge5.fiap.com.msPagamento.dto.request.PaymentRequestDTO;
import techclallenge5.fiap.com.msPagamento.dto.request.PaymentUpdateMethodRequestDTO;
import techclallenge5.fiap.com.msPagamento.dto.response.PaymentResponseDTO;
import techclallenge5.fiap.com.msPagamento.integration.ShoppingCartClient;
import techclallenge5.fiap.com.msPagamento.model.Payment;
import techclallenge5.fiap.com.msPagamento.model.PaymentMethod;
import techclallenge5.fiap.com.msPagamento.model.PaymentStatus;
import techclallenge5.fiap.com.msPagamento.model.ShoppingCart;
import techclallenge5.fiap.com.msPagamento.repository.PaymentRepository;
import techclallenge5.fiap.com.msPagamento.util.pixUtil.PixPayloadGenerator;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class PaymentServiceImplTest {
    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PixPayloadGenerator pixPayloadGenerator;

    @Mock
    private ShoppingCartClient shoppingCartClient;

    @Mock
    private PixService pixService;

    @InjectMocks
    private PaymentServiceImpl paymentServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreatePaymentOrderSuccessfully() {
        String paymentId = UUID.randomUUID().toString();
        Double amount = 1259.90;
        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO("orderId1", PaymentMethod.CREDIT_CARD);
        ShoppingCart cart = new ShoppingCart("123", 1L, amount);
        Payment payment = new Payment(paymentRequestDTO.orderId(), cart.getTotalValue(), paymentRequestDTO.paymentMethod());
        Payment savedPayment = new Payment(paymentId,paymentRequestDTO.orderId(),amount, PaymentMethod.CREDIT_CARD,  PaymentStatus.PENDING, LocalDateTime.now(), LocalDateTime.now().plusDays(5));

        when(paymentRepository.save(payment)).thenReturn(savedPayment);

        // Act
        PaymentResponseDTO response = paymentServiceImpl.createPaymentOrder(paymentRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(savedPayment.getId(), response.id());
        assertEquals(savedPayment.getOrderId(), response.shoppingCartId());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void shouldUpdatePaymentMethodSuccessfully() {
        // Arrange
        String paymentId = UUID.randomUUID().toString();
        String orderId = "1";
        Double amount = 1259.90;
        PaymentUpdateMethodRequestDTO updateRequest = new PaymentUpdateMethodRequestDTO(PaymentMethod.DEBIT_CARD);
        Payment payment = new Payment(paymentId,orderId,amount, PaymentMethod.CREDIT_CARD,  PaymentStatus.PENDING, LocalDateTime.now(), LocalDateTime.now().plusDays(5));

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(paymentRepository.findByOrderId(payment.getOrderId())).thenReturn(Optional.of(payment));
        when(paymentRepository.save(payment)).thenReturn(payment);
//
        // Act
        PaymentResponseDTO response = paymentServiceImpl.updatePaymentMethod(updateRequest, payment.getId());

        // Assert
        assertNotNull(response);
        assertEquals(updateRequest.paymentMethod(), response.paymentMethod());
        verify(paymentRepository, times(2)).save(payment); // Called twice: once for setting start date and another for due date
    }

    @Test
    void shouldDeletePaymentSuccessfully() {
        String paymentId = UUID.randomUUID().toString();
        String orderId = "1";
        Double amount = 1259.90;
        Payment payment = new Payment(paymentId,orderId,amount, PaymentMethod.CREDIT_CARD,  PaymentStatus.PENDING, LocalDateTime.now(), LocalDateTime.now().plusDays(5));

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        doNothing().when(paymentRepository).deleteById(paymentId);

        // Act
        paymentServiceImpl.deletePayment(paymentId);

        // Assert
        verify(paymentRepository, times(1)).deleteById(paymentId);
    }

    @Test
    void shouldProcessPaymentSuccessfully() {
        // Arrange
        String paymentId = UUID.randomUUID().toString();
        String orderId = "1";
        Double amount = 1259.90;
        Payment payment = new Payment(paymentId,orderId,amount, PaymentMethod.CREDIT_CARD,  PaymentStatus.PENDING, LocalDateTime.now(), LocalDateTime.now().plusDays(5));

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(paymentRepository.findByOrderId(payment.getOrderId())).thenReturn(Optional.of(payment));
        when(paymentRepository.save(payment)).thenReturn(payment);

        Random random = mock(Random.class);
        when(random.nextDouble()).thenReturn(0.8); // Valor < SUCCESS_RATE = 0.85

        // Act
        PaymentResponseDTO response = paymentServiceImpl.processPayment(paymentId);

        // Assert
        assertNotNull(response);
        assertNotEquals(PaymentStatus.PENDING, response.status());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void shouldUpdatePaymentStatusSuccessfully() {
        String paymentId = UUID.randomUUID().toString();
        String orderId = "1";
        Double amount = 1259.90;
        Payment payment = new Payment(paymentId,orderId,amount, PaymentMethod.CREDIT_CARD,  PaymentStatus.PENDING, LocalDateTime.now(), LocalDateTime.now().plusDays(5));
        PaymentStatus newStatus = PaymentStatus.COMPLETED;

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(payment)).thenReturn(payment);

        // Act
        Payment updatedPayment = paymentServiceImpl.updatePaymentStatus(paymentId, newStatus);

        // Assert
        assertNotNull(updatedPayment);
        assertEquals(newStatus, updatedPayment.getPaymentStatus());
        verify(paymentRepository, times(1)).save(payment);
    }
}