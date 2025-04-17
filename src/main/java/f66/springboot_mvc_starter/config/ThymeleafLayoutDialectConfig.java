package f66.springboot_mvc_starter.config;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThymeleafLayoutDialectConfig {

    @Bean
    LayoutDialect layoutDialect() {

        return new LayoutDialect();
    }
}
