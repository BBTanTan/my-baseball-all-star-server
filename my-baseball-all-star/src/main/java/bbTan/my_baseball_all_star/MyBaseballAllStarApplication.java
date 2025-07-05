package bbTan.my_baseball_all_star;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MyBaseballAllStarApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyBaseballAllStarApplication.class, args);
	}

}
