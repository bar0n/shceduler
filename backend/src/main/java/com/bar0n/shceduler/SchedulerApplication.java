package com.bar0n.shceduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SchedulerApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SchedulerApplication.class);
	}


	@Bean
	public ConfigurableServletWebServerFactory webServerFactory() {

		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
		factory.addConnectorCustomizers(connector -> {

			if (System.getenv("PORT") != null) {
				connector.setPort(Integer.valueOf(System.getenv("PORT")));

			}
		});
		return factory;
	}

/*	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return (container -> {
			container.setContextPath("");
			if (System.getenv("PORT") != null) {
				container.setPort(Integer.valueOf(System.getenv("PORT")));

			}
		});
	}*/


	public static void main(String[] args) {
		SpringApplication.run(SchedulerApplication.class, args);
	}
}
