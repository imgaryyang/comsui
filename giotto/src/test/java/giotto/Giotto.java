package giotto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = { "com.suidifu", "com.zufangbao", "giotto" })
@SpringBootApplication
public class Giotto extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Giotto.class);
    }

    public static void main(String[] args) throws Exception {

        SpringApplication.run(Giotto.class, args);
    }

}