package kz.aaslnv.csgo.easycontracts;

import kz.aaslnv.csgo.easycontracts.calculator.AllCollectionsContractCalculator;
import kz.aaslnv.csgo.easycontracts.calculator.IContractCalculator;
import kz.aaslnv.csgo.easycontracts.calculator.OneCollectionContractCalculator;
import kz.aaslnv.csgo.easycontracts.collection.model.Collection;
import kz.aaslnv.csgo.easycontracts.collection.service.CollectionService;
import kz.aaslnv.csgo.easycontracts.contract.model.Contract;
import kz.aaslnv.csgo.easycontracts.parser.CsgoExchangeCollectionParser;
import kz.aaslnv.csgo.easycontracts.parser.IParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootApplication
public class EasyContractsApplication {

    public static final String ALL_COLLECTIONS_CONTRACT_TYPE = "all";

    private static IContractCalculator contractCalculator;

    private static IParser<Collection> collectionsParser;

    private static CollectionService collectionService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(EasyContractsApplication.class, args);
        Environment env = context.getEnvironment();
        boolean parseCollections = Boolean.parseBoolean(env.getProperty("application.contract.parse_collections"));
        boolean parsePrices = Boolean.parseBoolean(env.getProperty("application.contract.parse_prices"));
        boolean isStatTrak = Boolean.parseBoolean(env.getProperty("application.contract.stat_trak"));
        String contractType = env.getProperty("application.contract.collection_type");
        collectionService = context.getBean(CollectionService.class);

        if (ALL_COLLECTIONS_CONTRACT_TYPE.equals(contractType)){
            contractCalculator = context.getBean(AllCollectionsContractCalculator.class);
        } else {
            contractCalculator = context.getBean(OneCollectionContractCalculator.class);
        }

        if (parseCollections){
            collectionsParser = context.getBean(CsgoExchangeCollectionParser.class);
            parseCollectionsAndUpdateDb();
        }

        if (parsePrices){
            parsePricesAndUpdateDb();
        }



        writeResult(new ArrayList<>());
    }

    private static void parseCollectionsAndUpdateDb(){
        List<Collection> collections = collectionsParser.parse();
        collectionService.saveAll(collections);
    }

    private static void parsePricesAndUpdateDb(){
        System.out.println("Будет добавлен функционал парсинга цен");
    }

    private static List<Contract> calculate(boolean isStatTrak){
        return contractCalculator.calculate(isStatTrak);
    }

    private static void writeResult(List<Contract> contracts){
        System.out.println("Будет добавлен функционал сохранения контрактов");
    }
}
