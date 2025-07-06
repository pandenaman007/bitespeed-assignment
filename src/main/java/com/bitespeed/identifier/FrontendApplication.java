package com.bitespeed.identifier;
import java.util.Map;
import java.util.HashMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FrontendApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(FrontendApplication.class);

		// Use PORT env var if present (used by Render), else default to 8081
		Map<String, Object> props = new HashMap<>();
		String port = System.getenv("PORT");
		if (port != null) {
			props.put("server.port", port);
		} else {
			props.put("server.port", "8081");
		}
		app.setDefaultProperties(props);
		app.run(args);
	}

}
