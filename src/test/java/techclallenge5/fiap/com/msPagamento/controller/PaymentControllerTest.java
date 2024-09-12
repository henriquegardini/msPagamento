package techclallenge5.fiap.com.msPagamento.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import techclallenge5.fiap.com.msPagamento.dto.request.PaymentRequestDTO;
import techclallenge5.fiap.com.msPagamento.dto.request.PaymentUpdateMethodRequestDTO;
import techclallenge5.fiap.com.msPagamento.dto.response.PaymentPixResponseDTO;
import techclallenge5.fiap.com.msPagamento.dto.response.PaymentResponseDTO;
import techclallenge5.fiap.com.msPagamento.model.Payment;
import techclallenge5.fiap.com.msPagamento.model.PaymentMethod;
import techclallenge5.fiap.com.msPagamento.model.PaymentStatus;
import techclallenge5.fiap.com.msPagamento.service.PaymentServiceImpl;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class PaymentControllerTest {

    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private PaymentServiceImpl paymentServiceImpl;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    @Test
    void whenPaymentMethodPix_shouldCreatePixPaymentResponse() {
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO("123", PaymentMethod.PIX);
        PaymentPixResponseDTO paymentPixResponse = new PaymentPixResponseDTO(
                                                        UUID.randomUUID().toString() ,"123",1259.90,PaymentMethod.PIX,UUID.randomUUID().toString(), PaymentStatus.PENDING, LocalDateTime.now(),LocalDateTime.now().plusHours(6));

        when(paymentServiceImpl.createPixPaymentOrder(paymentRequest)).thenReturn(paymentPixResponse);

        // Act
        ResponseEntity<?> response = paymentController.createPayment(paymentRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(paymentPixResponse, response.getBody());
        verify(paymentServiceImpl, times(1)).createPixPaymentOrder(paymentRequest);
    }

    @Test
    void shouldCreateOtherPaymentSuccessfully() {
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO("123", PaymentMethod.CREDIT_CARD);
        PaymentResponseDTO paymentResponse = new PaymentResponseDTO(
                UUID.randomUUID().toString() ,"123",1259.90,PaymentMethod.CREDIT_CARD, PaymentStatus.PENDING, LocalDateTime.now(),LocalDateTime.now().plusHours(6));

        when(paymentServiceImpl.createPaymentOrder(paymentRequest)).thenReturn(paymentResponse);

        // Act
        ResponseEntity<?> response = paymentController.createPayment(paymentRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(paymentResponse, response.getBody());
        verify(paymentServiceImpl, times(1)).createPaymentOrder(paymentRequest);
    }

    @Test
    void shouldGetPaymentByIdSuccessfully() {
        String paymentId = UUID.randomUUID().toString();
        Double amount = 1259.90;
        Payment payment = new Payment("1L", amount, PaymentMethod.CREDIT_CARD);
        PaymentResponseDTO paymentResponse = new PaymentResponseDTO(
                paymentId ,"123",amount,PaymentMethod.CREDIT_CARD, PaymentStatus.PENDING, LocalDateTime.now(),LocalDateTime.now().plusHours(6));

        when(paymentServiceImpl.findPaymentById(paymentId)).thenReturn(payment);
        when(paymentServiceImpl.toDTO(payment)).thenReturn(paymentResponse);

        // Act
        ResponseEntity<PaymentResponseDTO> response = paymentController.getPayment(paymentId, false);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(paymentResponse, response.getBody());
        verify(paymentServiceImpl, times(1)).findPaymentById(paymentId);
    }

    @Test
    void shouldGetPaymentByShoppingCartIdSuccessfully() {
        // Arrange
        String shoppingCartId = "123";
        Double amount = 1259.90;
        Payment payment = new Payment(shoppingCartId, amount, PaymentMethod.CREDIT_CARD);
        PaymentResponseDTO paymentResponse = new PaymentResponseDTO(
                UUID.randomUUID().toString() ,"123",amount,PaymentMethod.CREDIT_CARD, PaymentStatus.PENDING, LocalDateTime.now(),LocalDateTime.now().plusHours(6));

        when(paymentServiceImpl.findPaymentByOrderId(shoppingCartId)).thenReturn(payment);
        when(paymentServiceImpl.toDTO(payment)).thenReturn(paymentResponse);

        // Act
        ResponseEntity<PaymentResponseDTO> response = paymentController.getPayment(shoppingCartId, true);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(paymentResponse, response.getBody());
        verify(paymentServiceImpl, times(1)).findPaymentByOrderId(shoppingCartId);
    }

    @Test
    void shouldUpdatePaymentMethodSuccessfully() {
        // Arrange
        String paymentId = "123";
        Double amount = 1259.90;
        PaymentUpdateMethodRequestDTO updateRequest = new PaymentUpdateMethodRequestDTO(PaymentMethod.CREDIT_CARD);
        PaymentResponseDTO updatedResponse = new PaymentResponseDTO(
                UUID.randomUUID().toString() ,"123",amount,PaymentMethod.CREDIT_CARD, PaymentStatus.PENDING, LocalDateTime.now(),LocalDateTime.now().plusHours(6));


        when(paymentServiceImpl.updatePaymentMethod(updateRequest, paymentId)).thenReturn(updatedResponse);

        // Act
        ResponseEntity<PaymentResponseDTO> response = paymentController.updatePaymentMethodById(paymentId, updateRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updatedResponse, response.getBody());
        verify(paymentServiceImpl, times(1)).updatePaymentMethod(updateRequest, paymentId);
    }

    @Test
    void shouldProcessPaymentSuccessfully() {
        // Arrange
        String paymentId = "123";
        Double amount = 1259.90;
        String authToken = "AUTHTOKEN";
        PaymentResponseDTO processedResponse = new PaymentResponseDTO(
                UUID.randomUUID().toString() ,"123",amount,PaymentMethod.CREDIT_CARD, PaymentStatus.COMPLETED, LocalDateTime.now(),LocalDateTime.now().plusHours(6));
        ; // Configure o DTO conforme necessário

        when(paymentServiceImpl.processPayment(paymentId, authToken)).thenReturn(processedResponse);

        // Act
        ResponseEntity<PaymentResponseDTO> response = paymentController.processPayment(paymentId, authToken);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(processedResponse, response.getBody());
        verify(paymentServiceImpl, times(1)).processPayment(paymentId,authToken);
    }

    @Test
    void shouldDeletePaymentSuccessfully() {
        // Arrange
        String paymentId = "123";

        doNothing().when(paymentServiceImpl).deletePayment(paymentId);

        // Act
        ResponseEntity<?> response = paymentController.deletePayment(paymentId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(paymentServiceImpl, times(1)).deletePayment(paymentId);
    }
}