package techclallenge5.fiap.com.msPagamento.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techclallenge5.fiap.com.msPagamento.util.pixUtil.PixPayloadGenerator;

@Service
public class PixService {

    private PixPayloadGenerator pixPayloadGenerator;

    public PixService(PixPayloadGenerator pixPayloadGenerator) {
        this.pixPayloadGenerator = pixPayloadGenerator;
    }

    public String createPixCode(double valor, String transactionId) {
        String payload = pixPayloadGenerator.generatePayload(valor, transactionId);
        return payload;
    }
}
