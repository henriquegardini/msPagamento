package techclallenge5.fiap.com.msPagamento.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import techclallenge5.fiap.com.msPagamento.dto.PaymentRequestDTO;
import techclallenge5.fiap.com.msPagamento.dto.PaymentResponseDTO;
import techclallenge5.fiap.com.msPagamento.exception.PaymentInvalidStatusException;
import techclallenge5.fiap.com.msPagamento.exception.PaymentNotFoundException;
import techclallenge5.fiap.com.msPagamento.integration.ShoppingCartClient;
import techclallenge5.fiap.com.msPagamento.model.Payment;
import techclallenge5.fiap.com.msPagamento.model.PaymentStatus;
import techclallenge5.fiap.com.msPagamento.model.ShoppingCart;
import techclallenge5.fiap.com.msPagamento.repository.PaymentRepository;
import techclallenge5.fiap.com.msPagamento.util.pixUtil.PixPayloadGenerator;

import java.util.Random;

import static techclallenge5.fiap.com.msPagamento.model.PaymentStatus.COMPLETED;
import static techclallenge5.fiap.com.msPagamento.model.PaymentStatus.FAILED;

@Service
public class PaymentServiceImpl implements PaymentService{
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
        if(validatePayment(paymentRequestDTO.orderId())){
            throw new PaymentInvalidStatusException();
        }
        try{
            ShoppingCart cart = shoppingCartClient.getCartDetails(paymentRequestDTO.orderId()).block();
            Payment payment = new Payment(paymentRequestDTO.orderId(), cart.getTotalAmount(), paymentRequestDTO.paymentMethod());
            return toDTO(paymentRepository.save(payment));
        }catch(Exception ex){
            throw new PaymentNotFoundException();
        }
    }

    public PaymentResponseDTO createPixPaymentOrder(PaymentRequestDTO paymentRequestDTO) {
//        if(validatePayment(paymentRequestDTO.orderId())){
//            throw new PaymentInvalidStatusException();
//        }
//        try{
//            ShoppingCart cart = shoppingCartClient.getCartDetails(paymentRequestDTO.orderId()).block();
//            Payment payment = new Payment(paymentRequestDTO.orderId(), cart.getTotalAmount(), paymentRequestDTO.paymentMethod());
//            return paymentRepository.save(payment);
////        }catch(Exception ex){
////            throw new PaymentNotFoundException();
////        }
        return null;
    }

    @Transactional
    public PaymentResponseDTO updatePaymentMethod(PaymentRequestDTO paymentRequestDTO) {
        Payment payment = this.findPaymentById(paymentRequestDTO.orderId());
        validatePayment(payment.getOrderId());
        payment.setPaymentMethod(paymentRequestDTO.paymentMethod());
        return toDTO(paymentRepository.save(payment));
    }

    @Transactional
    public void deletePayment(String id) {
        Payment payment = findPaymentById(id);
        paymentRepository.deleteById(payment.getId());
    }

    @Transactional
    public PaymentResponseDTO processPayment(String id) {
        Payment payment = this.findPaymentById(id);
        if(validatePayment(payment.getOrderId())){
            throw new PaymentInvalidStatusException();
        }
        final double SUCCESS_RATE = 0.85;
        Random random = new Random();
        boolean isSuccess = random.nextDouble() < SUCCESS_RATE;
        if(isSuccess){
            payment.setPaymentStatus(COMPLETED);
            ShoppingCart cart = shoppingCartClient.getCartDetails(payment.getOrderId()).block();
            shoppingCartClient.updateCartStatus(cart.getUserId());
        }
        else{
            payment.setPaymentStatus(FAILED);
        }
        return toDTO(paymentRepository.save(payment));
    }

    public Payment findPaymentByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(()-> new PaymentNotFoundException());
    }

    public Payment findPaymentById(String id) {
        return paymentRepository.findById(id)
                .orElseThrow(()-> new PaymentNotFoundException());
    }

    @Transactional
    public Payment updatePaymentStatus(String paymentId, PaymentStatus newStatus) {
      Payment payment = findPaymentById(paymentId);
      payment.setPaymentStatus(newStatus);
      return paymentRepository.save(payment);
    }

    private boolean validatePayment(String orderId) {
        Payment payment = this.findPaymentByOrderId(orderId);
        if (payment == null || payment.getPaymentStatus() == COMPLETED) {
            return false;
        }
        return true;
    }

    public PaymentResponseDTO toDTO(Payment payment){
        return new PaymentResponseDTO(
                payment.getId(),
                payment.getPaymentMethod(),
                payment.getPaymentStartDate(),
                payment.getPaymentDueDate());
    }
}

