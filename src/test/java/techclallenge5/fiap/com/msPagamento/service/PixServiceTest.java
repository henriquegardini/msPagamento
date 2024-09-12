package techclallenge5.fiap.com.msPagamento.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import techclallenge5.fiap.com.msPagamento.util.pixUtil.PixPayloadGenerator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PixServiceTest {

    @Mock
    private PixPayloadGenerator pixPayloadGenerator;

    @InjectMocks
    private PixService pixService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreatePixCodeSuccessfully() {
        // Arrange
        double valor = 100.0;
        String transactionId = "123456";
        String expectedPayload = "PIX_PAYLOAD_SAMPLE";

        when(pixPayloadGenerator.generatePayload(valor, transactionId)).thenReturn(expectedPayload);

        // Act
        String actualPayload = pixService.createPixCode(valor, transactionId);

        // Assert
        assertEquals(expectedPayload, actualPayload);
        verify(pixPayloadGenerator, times(1)).generatePayload(valor, transactionId);
    }
}