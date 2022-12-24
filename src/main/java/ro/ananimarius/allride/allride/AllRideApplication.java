package ro.ananimarius.allride.allride;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//@SpringBootApplication
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class AllRideApplication {

	public static void main(String[] args) {

		SpringApplication.run(AllRideApplication.class, args);
	}

}
