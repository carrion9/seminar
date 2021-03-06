package com.vetx.starter;

import com.fasterxml.jackson.datatype.jsr310.deser.JSR310StringParsableDeserializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EntityScan(basePackageClasses = {
    StarterApplication.class,
    Jsr310JpaConverters.class,
    JSR310StringParsableDeserializer.class
})
public class StarterApplication {

  public static void main(String[] args) {
    SpringApplication.run(StarterApplication.class, args);
  }

  @PostConstruct
  void init() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }
}
