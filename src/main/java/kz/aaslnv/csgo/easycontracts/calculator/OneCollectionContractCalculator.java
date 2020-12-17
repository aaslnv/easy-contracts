package kz.aaslnv.csgo.easycontracts.calculator;

import kz.aaslnv.csgo.easycontracts.collection.model.Collection;
import kz.aaslnv.csgo.easycontracts.collection.service.CollectionService;
import kz.aaslnv.csgo.easycontracts.contract.model.Contract;
import kz.aaslnv.csgo.easycontracts.contract.model.ContractItem;
import kz.aaslnv.csgo.easycontracts.contract.service.ContractItemService;
import kz.aaslnv.csgo.easycontracts.enumiration.ItemQuality;
import kz.aaslnv.csgo.easycontracts.enumiration.ItemRarity;
import kz.aaslnv.csgo.easycontracts.item.model.Item;
import kz.aaslnv.csgo.easycontracts.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static kz.aaslnv.csgo.easycontracts.util.Constant.CONTRACT_REQUIRED_ITEMS_COUNT;

@Service
@Qualifier("one")
public class OneCollectionContractCalculator implements IContractCalculator {

    private final CollectionService collectionService;
    private final ItemService itemService;
    private final Calculator calculator;
    private final ContractItemService contractItemService;

    @Autowired
    public OneCollectionContractCalculator(CollectionService collectionService, ItemService itemService,
                                           Calculator calculator, ContractItemService contractItemService) {
        this.collectionService = collectionService;
        this.itemService = itemService;
        this.calculator = calculator;
        this.contractItemService = contractItemService;
    }

    public List<Contract> calculate(boolean isStatTrak) {
        List<Contract> contracts = new ArrayList<>();
        List<Collection> collections = collectionService.getAll();

        collections.forEach(collection -> {

            List<ItemRarity> itemRarities = collection.getItems().stream()
                    .map(Item::getRarity)
                    .distinct()
                    .collect(Collectors.toList());

            for (ItemRarity rarity : itemRarities) {
                List<Contract> resultContracts = processCalculateContract(collection.getItems(), rarity, isStatTrak);
                if (Objects.nonNull(resultContracts)) {
                    contracts.addAll(resultContracts);
                }
            }
        });
        return contracts;
    }

    public List<Contract> processCalculateContract(List<Item> items, ItemRarity rarity, boolean isStatTrak) {
        List<Contract> contracts = new ArrayList<>();
        double step = 0.001;
        double minFloat = itemService.getMinFloatByItemsAndRarity(items, rarity);
        double maxFloat = itemService.getMaxFloatByItemsAndRarity(items, rarity);
        ItemRarity nextRarity = ItemRarity.getRarityByPriority(rarity.getPriority() + 1)
                .orElseThrow(() -> new RuntimeException("Incorrect priority of rarity"));

        if (!nextRarity.isCanBeTraded()) {
            return null;
        }

        List<Item> nextRarityItems = items.stream()
                .filter(item -> item.getRarity() == nextRarity)
                .collect(Collectors.toList());

        for (double i = maxFloat - step; i > minFloat; i -= step) {
            Contract contract;
            ContractItem requiredItem;
            List<ContractItem> requiredItems;
            List<ContractItem> resultItems;
            Contract lastContract = contracts.stream()
                    .reduce((first, second) -> second)
                    .orElse(null);

            if (i == 0.45 || i == 0.38 || i == 0.15 || i == 0.07 || i == 0) {
                continue;
            }

            resultItems = new ArrayList<>();
            requiredItem = contractItemService.getContractItemWithLowestPriceByRarityAndFloat(items, rarity, i, isStatTrak);
            requiredItems = new ArrayList<>(10);

            for (int j = 0; j < CONTRACT_REQUIRED_ITEMS_COUNT; j++) {
                requiredItems.add(requiredItem);
            }

            for (Item item : nextRarityItems) {
                double resultFloat = calculator.calculateResultFloat(item.getMinFloat(), item.getMaxFloat(), i);
                ItemQuality quality = calculator.getQualityByFloat(resultFloat);
                Optional<ContractItem> contractItemOptional = contractItemService.map(item, quality, isStatTrak);
                contractItemOptional.ifPresent(requiredItems::add);
            }

            BigDecimal contractPrice = requiredItem.getPrice().multiply(new BigDecimal(CONTRACT_REQUIRED_ITEMS_COUNT));
            double profitability = calculator.calculateProfitability(contractPrice, resultItems);

            contract = new Contract(contractPrice, i, i, requiredItems, resultItems, profitability);

            if (contracts.size() == 0 || !lastContract.getResultItems().equals(resultItems)) {
                contracts.add(contract);
            } else {
                lastContract.setMinFloat(lastContract.getMinFloat() - step);
            }
        }
        return contracts;
    }
}
