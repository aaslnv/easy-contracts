package kz.aaslnv.csgo.easycontracts;

import kz.aaslnv.csgo.easycontracts.calculator.IContractCalculator;
import kz.aaslnv.csgo.easycontracts.collection.model.Collection;
import kz.aaslnv.csgo.easycontracts.collection.service.CollectionService;
import kz.aaslnv.csgo.easycontracts.contract.model.Contract;
import kz.aaslnv.csgo.easycontracts.enumiration.TradeMarket;
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

    @Resource(name = "${application.contract.trade_market}")
    private IParser<ItemPrice> pricesParser;

    @Value("${application.contract.parse_collections}")
    private boolean parseCollections;

    @Value("${application.contract.parse_prices}")
    private boolean parsePrices;

    @Value("${application.contract.trade_market}")
    private TradeMarket tradeMarket;

    private final CollectionService collectionService;

    private final ItemPriceService itemPriceService;

    private final IParser<Collection> collectionsParser;


    public EasyContractsApplicationStarter(CollectionService collectionService, ItemPriceService itemPriceService,
                                           IParser<Collection> collectionsParser) {
        this.collectionService = collectionService;
        this.itemPriceService = itemPriceService;
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
            itemPriceService.deleteAllByTradeMarket(tradeMarket);
            itemPriceService.saveAll(prices);
            log.info("Prices parsing finished. All prices are up-to-date");
        }

        log.info("Contracts calculating started");
        List<Contract> contracts = contractCalculator.calculate();
        log.info("Contracts calculating finished. Found {} contracts", contracts.size());

        log.info("Contracts result writing started");
        resultWriter.write(contracts);
        log.info("Contracts result writing finished");
    }
}
