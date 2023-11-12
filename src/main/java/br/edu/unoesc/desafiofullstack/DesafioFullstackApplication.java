package br.edu.unoesc.desafiofullstack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.edu.unoesc.desafiofullstack.interceptors.AuthInterceptor;

@SpringBootApplication
public class DesafioFullstackApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(DesafioFullstackApplication.class, args);
	}

	private final AuthInterceptor authInterceptor;

	public DesafioFullstackApplication(AuthInterceptor authInterceptor) {
		this.authInterceptor = authInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor).excludePathPatterns("/static/**");
	}

}
