package techclallenge5.fiap.com.msPagamento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsPagamentoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsPagamentoApplication.class, args);
	}

}
