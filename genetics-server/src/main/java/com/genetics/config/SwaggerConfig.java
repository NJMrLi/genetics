package com.genetics.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger / OpenAPI 配置
 * 访问地址：http://localhost:8080/swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("VAT & EPR 动态表单系统 API")
                        .version("1.0.0")
                        .description("包含自定义控件、服务单模板、服务单实例等接口")
                        .contact(new Contact()
                                .name("Genetics Team")
                        )
                );
    }
}
