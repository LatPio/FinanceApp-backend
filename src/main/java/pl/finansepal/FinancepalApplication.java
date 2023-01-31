package pl.finansepal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@PropertySource("classpath:secret.properties")
@EnableAsync

public class FinancepalApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinancepalApplication.class, args);
	}

}
