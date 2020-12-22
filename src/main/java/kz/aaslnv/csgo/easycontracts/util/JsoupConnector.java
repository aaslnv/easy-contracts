package kz.aaslnv.csgo.easycontracts.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsoupConnector {

    public static final String JSOUP_CONNECTION_USER_AGENT = "Mozilla";
    public static final int JSOUP_CONNECTION_TIMEOUT = 5000;

    public Document connect(String url) throws IOException {
        Connection connection = Jsoup.connect(url);
        connection.userAgent(JSOUP_CONNECTION_USER_AGENT);
        connection.timeout(JSOUP_CONNECTION_TIMEOUT);
        return connection.get();
    }
}
