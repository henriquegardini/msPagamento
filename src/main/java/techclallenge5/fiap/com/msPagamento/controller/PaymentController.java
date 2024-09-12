package techclallenge5.fiap.com.msPagamento.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techclallenge5.fiap.com.msPagamento.dto.response.PaymentPixResponseDTO;
import techclallenge5.fiap.com.msPagamento.dto.request.PaymentRequestDTO;
import techclallenge5.fiap.com.msPagamento.dto.response.PaymentResponseDTO;
import techclallenge5.fiap.com.msPagamento.dto.request.PaymentUpdateMethodRequestDTO;
import techclallenge5.fiap.com.msPagamento.model.Payment;
import techclallenge5.fiap.com.msPagamento.model.PaymentMethod;
import techclallenge5.fiap.com.msPagamento.service.PaymentServiceImpl;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentServiceImpl paymentServiceImpl;

    public PaymentController(PaymentServiceImpl paymentServiceImpl) {
        this.paymentServiceImpl = paymentServiceImpl;
    }

    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequestDTO payment) {
      if(payment.paymentMethod()== PaymentMethod.PIX){
          PaymentPixResponseDTO response = paymentServiceImpl.createPixPaymentOrder(payment);
          return ResponseEntity.created(UriComponentsBuilder.fromPath("/payments/{id}").buildAndExpand(response.id()).toUri())
                  .body(response);
      }
      PaymentResponseDTO response = paymentServiceImpl.createPaymentOrder(payment);
        return ResponseEntity.created(UriComponentsBuilder.fromPath("/payments/{id}").buildAndExpand(response.id()).toUri())
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getPayment(@PathVariable String id,
                                                         @RequestParam(required = false, defaultValue = "false") boolean byShoppingCartId) {
        Payment payment;
        if (byShoppingCartId) {
            payment = paymentServiceImpl.findPaymentByOrderId(id);
        } else {
            payment = paymentServiceImpl.findPaymentById(id);
        }
        return ResponseEntity.ok(paymentServiceImpl.toDTO(payment));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> updatePaymentMethodById(@PathVariable String id, @RequestBody PaymentUpdateMethodRequestDTO paymentUpdateRequestDTO) {
        PaymentResponseDTO payment = paymentServiceImpl.updatePaymentMethod(paymentUpdateRequestDTO, id);
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<PaymentResponseDTO> processPayment(@PathVariable String id, @RequestHeader("Authorization") String authToken){

        PaymentResponseDTO payment = paymentServiceImpl.processPayment(id, authToken);
        return ResponseEntity.ok(payment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePayment(@PathVariable String id){
        paymentServiceImpl.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
