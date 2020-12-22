package kz.aaslnv.csgo.easycontracts.writer;

import kz.aaslnv.csgo.easycontracts.contract.model.Contract;
import kz.aaslnv.csgo.easycontracts.contract.model.ContractItem;
import kz.aaslnv.csgo.easycontracts.enumiration.TradeMarket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.*;

@Slf4j
@Service(value = "file")
public class FileResultWriter implements IResultWriter{

    public static final String TRADE_MARKET_LINE = "Торговая площадка: %s \n";
    public static final String CONTRACTS_FOUND_LINE = "Контрактов найдено: %s \n";
    public static final String CONTRACTS_DELIMITER = "----------------------------------------------\n";
    public static final String COLLECTION_LINE = "Коллекция: %s \n";
    public static final String REQUIRED_ITEMS_LINE = "Необходимые предметы: \n";
    public static final String REQUIRED_ITEM_LINE = "%s %s - %s x%s. Мин. флоат = %s, макс. флоат = %s \n";
    public static final String CONTRACT_PRICE = "Стоимость контракта: %s \n";
    public static final String POSSIBLE_ITEMS_LINE = "Возможный результат: \n";
    public static final String POSSIBLE_ITEM_LINE = "%s %s - %s \n";
    public static final String PROFITABLE_LINE = "Профитность: %s%% \n";

    @Value("${application.contract.trade_market}")
    private TradeMarket tradeMarket;

    @Override
    public void write(List<Contract> contracts) {
        File file = new File("result.txt");

        try {
            if (!file.exists()){
                file.createNewFile();
            }
        } catch (IOException e){
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

        try (FileWriter writer = new FileWriter(file)){
            writer.write(format(TRADE_MARKET_LINE, tradeMarket));
            writer.write(format(CONTRACTS_FOUND_LINE, contracts.size()));
            writer.write(CONTRACTS_DELIMITER);
            for (Contract contract : contracts) {
                processWrite(writer, contract);
                writer.write(CONTRACTS_DELIMITER);
            }
            writer.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void processWrite(FileWriter writer, Contract contract) throws IOException {
        Map<ContractItem, Long> itemCountMap = new LinkedHashMap<>();
        DecimalFormat floatFormat = new DecimalFormat("#.####");
        DecimalFormat priceFormat = new DecimalFormat("#.##");
        BigDecimal sum = contract.getRequiredItems().stream()
                .map(ContractItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        contract.getRequiredItems().stream()
                .distinct()
                .forEach(contractItem -> {
                    Long count = contract.getRequiredItems().stream()
                            .filter(contractItem::equals)
                            .count();
                    itemCountMap.put(contractItem, count);
                });

        writer.write(format(COLLECTION_LINE, contract.getCollection().getName()));
        writer.write(REQUIRED_ITEMS_LINE);

        for (Map.Entry<ContractItem, Long> entry : itemCountMap.entrySet()) {
            ContractItem item = entry.getKey();
            writer.write(format(REQUIRED_ITEM_LINE, item.getName(), item.getQuality().getStyledName(), item.getPrice(),
                    entry.getValue(), floatFormat.format(contract.getMinFloat()), floatFormat.format(contract.getMaxFloat())));
        }

        writer.write(format(CONTRACT_PRICE, sum));
        writer.write(POSSIBLE_ITEMS_LINE);

        for (ContractItem item : contract.getResultItems()) {
            writer.write(format(POSSIBLE_ITEM_LINE, item.getName(), item.getQuality().getStyledName(), item.getPrice()));
        }

        writer.write(format(PROFITABLE_LINE, priceFormat.format(contract.getProfitability() * 100d)));

    }
}
