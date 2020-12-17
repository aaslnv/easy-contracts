package kz.aaslnv.csgo.easycontracts;

import kz.aaslnv.csgo.easycontracts.calculator.AllCollectionsContractCalculator;
import kz.aaslnv.csgo.easycontracts.calculator.IContractCalculator;
import kz.aaslnv.csgo.easycontracts.calculator.OneCollectionContractCalculator;
import kz.aaslnv.csgo.easycontracts.contract.model.Contract;
import kz.aaslnv.csgo.easycontracts.parser.CollectionParser;
import kz.aaslnv.csgo.easycontracts.parser.IParser;
import kz.aaslnv.csgo.easycontracts.parser.Parsable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.util.List;

@SpringBootApplication
public class EasyContractsApplication {

    public static final String ALL_COLLECTIONS_CONTRACT_TYPE = "all";

    private static IContractCalculator contractCalculator;

    private static IParser<? extends Parsable> parser;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(EasyContractsApplication.class, args);
        Environment env = context.getEnvironment();
        boolean parseCollections = Boolean.parseBoolean(env.getProperty("application.contract.parse_collections"));
        boolean parsePrices = Boolean.parseBoolean(env.getProperty("application.contract.parse_prices"));
        boolean isStatTrak = Boolean.parseBoolean(env.getProperty("application.contract.stat_trak"));
        String contractType = env.getProperty("application.contract.collection_type");

        if (ALL_COLLECTIONS_CONTRACT_TYPE.equals(contractType)){
            contractCalculator = context.getBean(AllCollectionsContractCalculator.class);
        } else {
            contractCalculator = context.getBean(OneCollectionContractCalculator.class);
        }

        if (parseCollections){
            parser = context.getBean(CollectionParser.class);
            parseCollectionsAndUpdateDB();
        }

        if (parsePrices){
            parser = context.getBean(CollectionParser.class);
            parsePricesAndUpdateDB();
        }

        List<Contract> contracts = start(isStatTrak);

        writeResult(contracts);
    }

    private static void parseCollectionsAndUpdateDB(){
        parser.parse();
    }

    private static void parsePricesAndUpdateDB(){

    }

    private static List<Contract> start(boolean isStatTrak){
        return contractCalculator.calculate(isStatTrak);
    }

    private static void writeResult(List<Contract> contracts){

    }
}
