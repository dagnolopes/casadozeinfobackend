package br.com.casadozeinfo.casadozeinfo_base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class CasadozeinfoBaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CasadozeinfoBaseApplication.class, args);
	}

}
