package com.genetics;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.genetics.mapper")
public class GeneticsApplication {
    public static void main(String[] args) {
        SpringApplication.run(GeneticsApplication.class, args);
    }
}
