package com.file.management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@MapperScan("com.file.management.**.mapper")
public class BatchFileApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchFileApplication.class, args);
	}

}
