package tech.mms.cos;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;

@Import(SpringConfig.class)
public class SpringBootApp {

  public static void main(String[] args) {
    var app = new SpringApplication(SpringBootApp.class);
    app.run(args);
  }
}
