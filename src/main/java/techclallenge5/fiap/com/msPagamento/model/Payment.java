package techclallenge5.fiap.com.msPagamento.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Table(name = "Payments")
@Entity(name = "Payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String orderId;
    private double amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;

    @CreationTimestamp
    private LocalDateTime paymentStartDate;

    private LocalDateTime paymentDueDate;

    public Payment(String orderId, double amount, PaymentMethod paymentMethod) {
        this.orderId = orderId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStartDate = LocalDateTime.now();
        this.paymentDueDate = calculateDueDate(paymentMethod);
    }

    private LocalDateTime calculateDueDate(PaymentMethod paymentMethod) {
        switch (paymentMethod) {
            case PIX:
                return this.paymentStartDate.plusDays(1);
            case DEBIT_CARD:
            case CREDIT_CARD:
                return this.paymentStartDate.plusDays(30);
            case BOLETO:
                return this.paymentStartDate.plusDays(7); // Vence em 7 dias
            default:
                throw new IllegalArgumentException("Payment method not supported");
        }
    }
}
