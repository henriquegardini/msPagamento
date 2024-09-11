package techclallenge5.fiap.com.msPagamento.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateMessage {
    private String field;
    private String message;
}
