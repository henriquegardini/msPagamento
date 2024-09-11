package techclallenge5.fiap.com.msPagamento.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import techclallenge5.fiap.com.msPagamento.dto.response.PaymentPixResponseDTO;
import techclallenge5.fiap.com.msPagamento.dto.request.PaymentRequestDTO;
import techclallenge5.fiap.com.msPagamento.dto.response.PaymentResponseDTO;
import techclallenge5.fiap.com.msPagamento.dto.request.PaymentUpdateMethodRequestDTO;
import techclallenge5.fiap.com.msPagamento.exception.*;
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

import static techclallenge5.fiap.com.msPagamento.model.PaymentStatus.COMPLETED;
import static techclallenge5.fiap.com.msPagamento.model.PaymentStatus.FAILED;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PixPayloadGenerator pixPayloadGenerator;
    private final ShoppingCartClient shoppingCartClient;

    public PaymentServiceImpl(PaymentRepository paymentRepository, PixPayloadGenerator pixPayloadGenerator, ShoppingCartClient shoppingCartClient) {
        this.paymentRepository = paymentRepository;
        this.pixPayloadGenerator = pixPayloadGenerator;
        this.shoppingCartClient = shoppingCartClient;
    }

    @Transactional
    public PaymentResponseDTO createPaymentOrder(PaymentRequestDTO paymentRequestDTO) {
        validatePaymentCreation(paymentRequestDTO.orderId());
//            ShoppingCart cart = shoppingCartClient.getCartDetails(paymentRequestDTO.orderId()).block();
        ShoppingCart cart = new ShoppingCart("123", 1L, 239.90);
        Payment payment = new Payment(paymentRequestDTO.orderId(), cart.getTotalAmount(), paymentRequestDTO.paymentMethod());
        return toDTO(paymentRepository.save(payment));
    }

    public PaymentPixResponseDTO createPixPaymentOrder(PaymentRequestDTO paymentRequestDTO) {
        validatePaymentCreation(paymentRequestDTO.orderId());
        ShoppingCart cart = new ShoppingCart("123", 1L, 239.90);
        Payment payment = new Payment(paymentRequestDTO.orderId(), cart.getTotalAmount(), paymentRequestDTO.paymentMethod());
        paymentRepository.save(payment);
        String pixCode = pixPayloadGenerator.generatePayload(payment.getAmount(), payment.getId());
        return toPixDTO(payment, pixCode);
    }

    @Transactional
    public PaymentResponseDTO updatePaymentMethod(PaymentUpdateMethodRequestDTO paymentRequestDTO, String id) {
        Payment payment = this.findPaymentById(id);
        validateUpdatePayment(payment.getOrderId(), paymentRequestDTO.paymentMethod());
        payment.setPaymentMethod(paymentRequestDTO.paymentMethod());
        payment.setPaymentStartDate(LocalDateTime.now());
        paymentRepository.save(payment);
        payment.setPaymentDueDate(payment.calculateDueDate(payment.getPaymentMethod()));
        paymentRepository.save(payment);
        return toDTO(payment);
    }

    @Transactional
    public void deletePayment(String id) {
        Payment payment = findPaymentById(id);
        paymentRepository.deleteById(payment.getId());
    }

    @Transactional
    public PaymentResponseDTO processPayment(String id) {
        Payment payment = this.findPaymentById(id);
        validatePayment(payment.getOrderId());
        final double SUCCESS_RATE = 0.85;
        Random random = new Random();
        boolean isSuccess = random.nextDouble() < SUCCESS_RATE;
        if (isSuccess) {
            payment.setPaymentStatus(COMPLETED);
//            ShoppingCart cart = shoppingCartClient.getCartDetails(payment.getOrderId()).block();
//            shoppingCartClient.updateCartStatus(cart.getUserId());
        } else {
            payment.setPaymentStatus(FAILED);
        }
        return toDTO(paymentRepository.save(payment));
    }

    public Payment findPaymentByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new PaymentNotFoundException());
    }

    public Payment findPaymentById(String id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException());
    }

    @Transactional
    public Payment updatePaymentStatus(String paymentId, PaymentStatus newStatus) {
        Payment payment = findPaymentById(paymentId);
        payment.setPaymentStatus(newStatus);
        return paymentRepository.save(payment);
    }

    private void validatePayment(String orderId) {
        Payment payment = this.findPaymentByOrderId(orderId);
        if (payment == null) {
            throw new PaymentNotFoundException();
        }
        if (payment.getPaymentStatus() == COMPLETED) {
            throw new PaymentInvalidStatusException();
        }
        if (payment.getPaymentDueDate().isBefore(LocalDateTime.now())) {
            throw new PaymentExpiredException();
        }
    }

    private void validatePaymentCreation(String orderId) {
        Optional<Payment> payment = paymentRepository.findByOrderId(orderId);
        if(payment.isPresent()){
            throw new PaymentAlreadyCreatedException();
        };
    }

    private void validateUpdatePayment(String orderId, PaymentMethod paymentRequestMethod) {
        Payment payment = this.findPaymentByOrderId(orderId);
        if (payment == null) {
            throw new PaymentNotFoundException();
        }
        if (payment.getPaymentStatus() == COMPLETED) {
            throw new PaymentInvalidStatusException();
        }
        if(payment.getPaymentMethod() == paymentRequestMethod){
            throw new PaymentInvalidMethodTransitionException();
        }
    }

    public PaymentResponseDTO toDTO(Payment payment) {
        return new PaymentResponseDTO(
                payment.getId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getPaymentStartDate(),
                payment.getPaymentDueDate());
    }

    public PaymentPixResponseDTO toPixDTO (Payment payment, String pixCode){
        return new PaymentPixResponseDTO(
                payment.getId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                pixCode,
                payment.getPaymentStatus(),
                payment.getPaymentStartDate(),
                payment.getPaymentDueDate());
    }
}

