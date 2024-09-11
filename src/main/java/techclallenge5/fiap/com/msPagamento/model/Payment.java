package techclallenge5.fiap.com.msPagamento.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private LocalDateTime paymentStartDate;

    private LocalDateTime paymentDueDate;

    public Payment(String orderId, double amount, PaymentMethod paymentMethod) {
        this.orderId = orderId;
        this.amount = amount;
        this.paymentStatus = PaymentStatus.PENDING;
        this.paymentMethod = paymentMethod;
        this.paymentStartDate = LocalDateTime.now();
        this.paymentDueDate = calculateDueDate(paymentMethod);
    }

    public LocalDateTime calculateDueDate(PaymentMethod paymentMethod) {
        switch (paymentMethod) {
            case PIX:
                return this.paymentStartDate.plusHours(5);
            case DEBIT_CARD:
                return this.paymentStartDate.plusDays(1);
            case CREDIT_CARD:
                return this.paymentStartDate.plusDays(3);
            case BOLETO:
                return this.paymentStartDate.plusDays(7);
            default:
                throw new IllegalArgumentException("Payment method not supported");
        }
    }
}
