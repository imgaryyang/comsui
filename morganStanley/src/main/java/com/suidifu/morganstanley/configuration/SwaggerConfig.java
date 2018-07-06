package com.suidifu.morganstanley.configuration;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

import static com.suidifu.morganstanley.configuration.SwaggerSpec.DATA_TYPE_STRING;
import static com.suidifu.morganstanley.configuration.SwaggerSpec.EMAIL;
import static com.suidifu.morganstanley.configuration.SwaggerSpec.HEADER;
import static com.suidifu.morganstanley.configuration.SwaggerSpec.MER_ID;
import static com.suidifu.morganstanley.configuration.SwaggerSpec.MER_ID_DESCRIPTION;
import static com.suidifu.morganstanley.configuration.SwaggerSpec.NAME;
import static com.suidifu.morganstanley.configuration.SwaggerSpec.SECRET;
import static com.suidifu.morganstanley.configuration.SwaggerSpec.SECRET_DESCRIPTION;
import static com.suidifu.morganstanley.configuration.SwaggerSpec.SIGN;
import static com.suidifu.morganstanley.configuration.SwaggerSpec.SIGN_DESCRIPTION;
import static com.suidifu.morganstanley.configuration.SwaggerSpec.TEST_MER_ID;
import static com.suidifu.morganstanley.configuration.SwaggerSpec.TEST_SECRET;
import static com.suidifu.morganstanley.configuration.SwaggerSpec.TITLE;
import static com.suidifu.morganstanley.configuration.SwaggerSpec.TITLE_DESCRIPTION;
import static com.suidifu.morganstanley.configuration.SwaggerSpec.URL;
import static com.suidifu.morganstanley.configuration.SwaggerSpec.VERSION;

/**
 * Swagger 配置
 *
 * @author louguanyang
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private Predicate<String> paths() {
        return Predicates.or(
                // 2.0接口
                PathSelectors.regex("/api/v2/.*"),
                // 3.0接口
                PathSelectors.regex("/api/v3/.*"),
                //前置接口
                PathSelectors.regex("/pre/api/.*")
        );
    }

    @Bean
    public Docket newsApi() {
        Parameter merId = new ParameterBuilder()
                .name(MER_ID)
                .description(MER_ID_DESCRIPTION)
                .modelRef(new ModelRef(DATA_TYPE_STRING))
                .parameterType(HEADER)
                .defaultValue(TEST_MER_ID)
                .required(true)
                .build();
        Parameter secret = new ParameterBuilder()
                .name(SECRET)
                .description(SECRET_DESCRIPTION)
                .modelRef(new ModelRef(DATA_TYPE_STRING))
                .parameterType(HEADER)
                .defaultValue(TEST_SECRET)
                .required(true)
                .build();
        Parameter sign = new ParameterBuilder()
                .name(SIGN)
                .description(SIGN_DESCRIPTION)
                .modelRef(new ModelRef(DATA_TYPE_STRING))
                .parameterType(HEADER)
                .defaultValue(StringUtils.EMPTY)
                .required(false)
                .build();

        List<Parameter> headers = Arrays.asList(merId, secret, sign);
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("morganStanley")
                .globalOperationParameters(headers)
                .apiInfo(apiInfo())
                .select()
                .paths(paths())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(TITLE)
                .description(TITLE_DESCRIPTION)
                .contact(new Contact(NAME, URL, EMAIL))
                .license("Apache License Version 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version(VERSION)
                .build();
    }
}