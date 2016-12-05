package pl.memleak.panel.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@ComponentScan({"pl.memleak.panel.presentation", "pl.memleak.panel.bootstrap"})
//Annotation below turns off basic authorization requirement during sending request(probably later will be deleted from here)
@EnableAutoConfiguration(exclude = {
		org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class
})
@PropertySource("classpath:application.properties")
public class BackendApplication extends SpringBootServletInitializer {

	private static final String HTTP_CORS_KEY = "http.cors";
	private Environment env;

	@Autowired
	public BackendApplication(Environment env) {
		this.env = env;
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return configureApplication(builder);
	}
	public static void main(String[] args) {
		configureApplication(new SpringApplicationBuilder()).run(args);
	}
	private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
		return builder.sources(BackendApplication.class).bannerMode(Banner.Mode.OFF);
	}

	@Bean
    public WebMvcConfigurer corsConfigurer() {

        return new WebMvcConfigurerAdapter() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(env.getProperty(HTTP_CORS_KEY));
            }
        };
    }
}
