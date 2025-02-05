package site.soobin.myselectshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyselectshopApplication {

  public static void main(String[] args) {
    SpringApplication.run(MyselectshopApplication.class, args);
  }
}
