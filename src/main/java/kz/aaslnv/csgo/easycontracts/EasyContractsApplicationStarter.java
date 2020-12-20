package kz.aaslnv.csgo.easycontracts;

import kz.aaslnv.csgo.easycontracts.calculator.IContractCalculator;
import kz.aaslnv.csgo.easycontracts.collection.model.Collection;
import kz.aaslnv.csgo.easycontracts.collection.service.CollectionService;
import kz.aaslnv.csgo.easycontracts.contract.model.Contract;
import kz.aaslnv.csgo.easycontracts.parser.IParser;
import kz.aaslnv.csgo.easycontracts.price.model.ItemPrice;
import kz.aaslnv.csgo.easycontracts.price.service.ItemPriceService;
import kz.aaslnv.csgo.easycontracts.writer.IResultWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class EasyContractsApplicationStarter {

    @Resource(name = "${application.contract.type}")
    private IContractCalculator contractCalculator;

    @Resource(name = "${application.contract.result}")
    private IResultWriter resultWriter;

    @Value("${application.contract.parse_collections}")
    private boolean parseCollections;

    @Value("${application.contract.parse_prices}")
    private boolean parsePrices;

    private final CollectionService collectionService;

    private final ItemPriceService itemPriceService;

    private final IParser<Collection> collectionsParser;

    private final IParser<ItemPrice> pricesParser;

    public EasyContractsApplicationStarter(CollectionService collectionService, ItemPriceService itemPriceService, IParser<ItemPrice> pricesParser, IParser<Collection> collectionsParser) {
        this.collectionService = collectionService;
        this.itemPriceService = itemPriceService;
        this.pricesParser = pricesParser;
        this.collectionsParser = collectionsParser;
    }

    public void start(){

        if (parseCollections){
            log.info("Collections parsing started");
            List<Collection> collections = collectionsParser.parse();
            List<Collection> addedCollections = collectionService.saveAll(collections);
            log.info("Collections parsing finished. Added {} new collections", addedCollections.size());
        }

        if (parsePrices){
            log.info("Prices parsing started");
            List<ItemPrice> prices = pricesParser.parse();
            itemPriceService.saveAll(prices);
            log.info("Prices parsing finished. All prices are up-to-date");
        }

        log.info("Contracts calculating started");
        List<Contract> contracts = contractCalculator.calculate();
        log.info("Contracts calculating finished. Found {} contracts", contracts.size());

        resultWriter.write(contracts);
    }
}
