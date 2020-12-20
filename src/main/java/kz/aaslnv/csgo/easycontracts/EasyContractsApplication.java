package kz.aaslnv.csgo.easycontracts;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class EasyContractsApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(EasyContractsApplication.class, args);
        EasyContractsApplicationStarter starter = context.getBean(EasyContractsApplicationStarter.class);
        starter.start();
    }
}
