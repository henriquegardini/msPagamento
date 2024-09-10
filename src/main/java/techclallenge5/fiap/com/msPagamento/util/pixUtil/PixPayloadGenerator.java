package techclallenge5.fiap.com.msPagamento.util.pixUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Component
public class PixPayloadGenerator {

    @Value("${pix.key}")
    private static String pixKey;


    @Value("${pix.vendor}")
    private static String pixVendor;

    @Value("${pix.city}")
    private static String pixCity;

    private static String normalizeString(String input) {
        return input.replaceAll("[^0-9a-zA-Z]+", "").toUpperCase(Locale.ROOT);
    }

    private static String calculateCRC16(String input) {
        byte[] data = input.getBytes(StandardCharsets.UTF_8);
        int crc = 0xFFFF;

        for (byte b : data) {
            crc ^= b & 0xFF;

            for (int i = 0; i < 8; i++) {
                if ((crc & 0x0001) != 0) {
                    crc = (crc >>> 1) ^ 0x8408;
                } else {
                    crc = crc >>> 1;
                }
            }
        }

        crc = ~crc;
        crc = (crc & 0xFFFF);
        return String.format("%04X", crc).toUpperCase();
    }

    public static String generatePayload(double value, String txId) {
        String payload = "00020126330014BR.GOV.BCB.PIX0114" + pixKey;
        if (value > 0) {
            payload += "5204000053039865404" + String.format("%.2f", value).replace(",", ".");
        }
        payload += "5802BR5915" + normalizeString(pixVendor) + "6008" + normalizeString(pixCity) + "62070503***" + normalizeString(txId) + "6304";

        String crc = calculateCRC16(payload);
        return payload + crc;
    }
}
