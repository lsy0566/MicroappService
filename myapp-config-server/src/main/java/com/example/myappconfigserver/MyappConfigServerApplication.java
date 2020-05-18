package com.example.myappconfigserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableConfigServer
public class MyappConfigServerApplication {


	public static void main(String[] args) {

		SpringApplication.run(MyappConfigServerApplication.class, args);

	}

}
