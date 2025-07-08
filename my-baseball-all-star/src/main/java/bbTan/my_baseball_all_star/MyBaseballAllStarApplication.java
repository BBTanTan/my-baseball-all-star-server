package bbTan.my_baseball_all_star;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableRetry
@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class MyBaseballAllStarApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyBaseballAllStarApplication.class, args);
	}

}
