package kz.aaslnv.csgo.easycontracts.configuration;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public OkHttpClient getHttpClient(){
        return new OkHttpClient();
    }
}
