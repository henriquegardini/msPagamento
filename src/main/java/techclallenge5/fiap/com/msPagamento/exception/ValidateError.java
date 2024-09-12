package techclallenge5.fiap.com.msPagamento.exception;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidateError extends StandardError{
    private List<ValidateMessage> messages = new ArrayList<ValidateMessage>();

    public List<ValidateMessage> getMessages() {
        return messages;
    }

    public void addMessages(String campo, String mensagem) {
        messages.add(new ValidateMessage(campo, mensagem));
    }
}
