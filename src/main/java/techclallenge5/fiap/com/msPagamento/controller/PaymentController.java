package techclallenge5.fiap.com.msPagamento.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techclallenge5.fiap.com.msPagamento.dto.PaymentRequestDTO;
import techclallenge5.fiap.com.msPagamento.dto.PaymentResponseDTO;
import techclallenge5.fiap.com.msPagamento.model.Payment;
import techclallenge5.fiap.com.msPagamento.model.PaymentMethod;
import techclallenge5.fiap.com.msPagamento.service.PaymentServiceImpl;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentServiceImpl paymentServiceImpl;

    public PaymentController(PaymentServiceImpl paymentServiceImpl) {
        this.paymentServiceImpl = paymentServiceImpl;
    }

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(@RequestBody PaymentRequestDTO payment) {
      if(payment.paymentMethod()== PaymentMethod.PIX){
          PaymentResponseDTO response = paymentServiceImpl.createPixPaymentOrder(payment);
          return ResponseEntity.ok(response);
      }
      PaymentResponseDTO response = paymentServiceImpl.createPaymentOrder(payment);
      return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByOrderId(@PathVariable String orderId) {
        Payment payment = paymentServiceImpl.findPaymentByOrderId(orderId);
        return ResponseEntity.ok(paymentServiceImpl.toDTO(payment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@PathVariable String id) {
        Payment payment = paymentServiceImpl.findPaymentById(id);
        return ResponseEntity.ok(paymentServiceImpl.toDTO(payment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> updatePaymentMethodById(@PathVariable String id, @RequestBody PaymentRequestDTO paymentRequestDTO) {
        PaymentResponseDTO payment = paymentServiceImpl.updatePaymentMethod(paymentRequestDTO);
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<PaymentResponseDTO> processPayment(@PathVariable String id){
        PaymentResponseDTO payment = paymentServiceImpl.processPayment(id);
        return ResponseEntity.ok(payment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePayment(@PathVariable String id){
        paymentServiceImpl.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
