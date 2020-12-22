package kz.aaslnv.csgo.easycontracts.parser;

import kz.aaslnv.csgo.easycontracts.collection.model.Collection;
import kz.aaslnv.csgo.easycontracts.item.model.ItemRarity;
import kz.aaslnv.csgo.easycontracts.item.model.Item;
import kz.aaslnv.csgo.easycontracts.item.service.ItemService;
import kz.aaslnv.csgo.easycontracts.util.JsoupConnector;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CsgoExchangeCollectionsParser implements IParser<Collection> {

    public static final String BASE_URL = "https://csgo.exchange/collection";
    public static final String COLLECTION_VIEW_ENDPOINT = "/view/";
    private final JsoupConnector jsoupConnector;
    private final ItemService itemService;

    @Autowired
    public CsgoExchangeCollectionsParser(JsoupConnector jsoupConnector, ItemService itemService) {
        this.jsoupConnector = jsoupConnector;
        this.itemService = itemService;
    }

    @Override
    public List<Collection> parse() {
        List<Collection> collections =  new ArrayList<>();
        Map<String, Long> collectionNames = parseCollectionNames();

        collectionNames.forEach((name, collectionId) -> {
            Collection collection = new Collection();
            List<Item> items = parseItems(collectionId, collection);
            collection.setName(name);
            collection.setItems(items);
            collections.add(collection);
        });
        return collections;
    }

    private Map<String, Long> parseCollectionNames(){
        Map<String, Long> collectionNames = new LinkedHashMap<>();
        Document document;

        try {
            document = jsoupConnector.connect(BASE_URL);
        } catch (IOException e){
            log.error("Could not connect to {}", BASE_URL);
            throw new RuntimeException(e);
        }

        List<Element> collectionElements = document.body().getElementsByClass("rItem");

        collectionElements.forEach(element -> {
            String name;
            Long collectionId;
            collectionId = element.getElementsByClass("imgItem sColl").stream()
                    .map(element1 -> Long.parseLong(element1.attr("data-idcollection")))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Collection id not found"));
            name = element.text().trim();
            collectionNames.put(name, collectionId);
        });
        return collectionNames;
    }

    private List<Item> parseItems(Long collectionId, Collection collection){
        Document document;

        try {
            log.info("Trying to connect to {}", BASE_URL + COLLECTION_VIEW_ENDPOINT + collectionId);
            document = jsoupConnector.connect(BASE_URL + COLLECTION_VIEW_ENDPOINT + collectionId);
        } catch (IOException e){
            log.error("Could not connect to {}", BASE_URL + COLLECTION_VIEW_ENDPOINT + collectionId);
            throw new RuntimeException(e);
        }

        List<Element> itemElements = document.body().getElementsByClass("vItem");

        return itemElements.stream()
                .map(element -> {
                    String name = element.attr("data-name");
                    ItemRarity rarity = itemService.getRarityByName(element.attr("data-quality"))
                            .orElseThrow(() -> new RuntimeException("Rarity not found"));
                    double minFloat = Double.parseDouble(element.attr("data-minwear"));
                    double maxFloat = Double.parseDouble(element.attr("data-maxwear"));
                    return new Item(name, minFloat, maxFloat, collection, rarity);
                })
                .collect(Collectors.toList());
    }
}
