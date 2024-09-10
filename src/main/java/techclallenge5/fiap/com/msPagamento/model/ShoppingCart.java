package techclallenge5.fiap.com.msPagamento.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {
    private String id;
    private Long userId;
    private double totalAmount;
}
