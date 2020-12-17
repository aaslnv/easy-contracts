package kz.aaslnv.csgo.easycontracts.parser;

import kz.aaslnv.csgo.easycontracts.collection.model.Collection;
import kz.aaslnv.csgo.easycontracts.util.JsoupConnector;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CollectionParser implements IParser<Collection> {

    public static final String url = "Some url";
    private final JsoupConnector jsoupConnector;

    @Autowired
    public CollectionParser(JsoupConnector jsoupConnector) {
        this.jsoupConnector = jsoupConnector;
    }

    @Override
    public List<Collection> parse() {
        Document document;

        try {
            document = jsoupConnector.connect(url);
        } catch (IOException e){
            log.error("Could not connect to {}", url);
            throw new RuntimeException(e);
        }

        return new ArrayList<>();
    }
}
