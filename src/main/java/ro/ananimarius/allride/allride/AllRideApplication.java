package ro.ananimarius.allride.allride;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.Entity;

//@SpringBootApplication
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
//@ComponentScan(basePackages = "ro.ananimarius.allride.allride.*")
//@EntityScan("ro.ananimarius.allride.allride.*")
//@EnableJpaRepositories(basePackages = "ro.ananimarius.allride.allride.CRUDinterfaces")
//https://github.com/spring-projects/spring-boot/issues/19603
public class AllRideApplication {

	public static void main(String[] args) {

		SpringApplication.run(AllRideApplication.class, args);
	}

}
