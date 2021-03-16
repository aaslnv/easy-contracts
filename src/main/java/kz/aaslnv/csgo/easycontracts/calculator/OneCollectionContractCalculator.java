package kz.aaslnv.csgo.easycontracts.calculator;

import kz.aaslnv.csgo.easycontracts.collection.model.Collection;
import kz.aaslnv.csgo.easycontracts.collection.service.CollectionService;
import kz.aaslnv.csgo.easycontracts.contract.model.Contract;
import kz.aaslnv.csgo.easycontracts.contract.model.ContractItem;
import kz.aaslnv.csgo.easycontracts.contract.service.ContractItemService;
import kz.aaslnv.csgo.easycontracts.enumiration.TradeMarket;
import kz.aaslnv.csgo.easycontracts.item.model.Item;
import kz.aaslnv.csgo.easycontracts.item.model.ItemQuality;
import kz.aaslnv.csgo.easycontracts.item.model.ItemRarity;
import kz.aaslnv.csgo.easycontracts.item.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static kz.aaslnv.csgo.easycontracts.util.Constant.CONTRACT_REQUIRED_ITEMS_COUNT;

@Slf4j
@Component(value = "one")
public class OneCollectionContractCalculator implements IContractCalculator {

    @Value("${application.contract.include_stat_trak}")
    private boolean includeStatTrak;
    @Value("${application.contract.trade_market}")
    private TradeMarket tradeMarket;
    @Value("${application.contract.min_floats_difference}")
    private double minFloatDifference;
    private final CollectionService collectionService;
    private final ItemService itemService;
    private final ContractItemService contractItemService;

    @Autowired
    public OneCollectionContractCalculator(CollectionService collectionService, ItemService itemService,
                                           ContractItemService contractItemService) {
        this.collectionService = collectionService;
        this.itemService = itemService;
        this.contractItemService = contractItemService;
    }

    public List<Contract> calculate() {
        List<Contract> contracts = new ArrayList<>();
        List<Collection> collections = collectionService.getAll();

        collections.forEach(collection -> {
            List<ItemRarity> itemRarities = collection.getItems().stream()
                    .map(Item::getRarity)
                    .distinct()
                    .filter(ItemRarity::isTradable)
                    .sorted(Comparator.comparing(ItemRarity::getPriority))
                    .collect(Collectors.toList());

            for (ItemRarity rarity : itemRarities) {
                List<Contract> resultContracts = processCalculateContract(collection, rarity);
                if (Objects.nonNull(resultContracts)) {
                    contracts.addAll(resultContracts);
                }
            }
        });
        return contracts.stream()
                .filter(contract -> contract.getProfitability() > 0)
                .filter(contract -> (contract.getMaxFloat() - contract.getMinFloat()) > minFloatDifference )
                .sorted(Comparator.comparing(Contract::getProfitability).reversed())
                .collect(Collectors.toList());
    }



    public List<Contract> processCalculateContract(Collection collection, ItemRarity rarity) {
        List<Contract> contracts = new ArrayList<>();
        List<Item> items = collection.getItems();
        double step = 0.001;
        double minFloat = itemService.getMinFloatByItemsAndRarity(items, rarity);
        double maxFloat = itemService.getMaxFloatByItemsAndRarity(items, rarity);
        ItemRarity nextRarity = itemService.getRarityByPriority(rarity.getPriority() + 1).orElse(null);

        if (Objects.isNull(nextRarity) || !nextRarity.isCanBeTraded() || !rarity.isTradable()) {
            return new ArrayList<>();
        }

        List<Item> nextRarityItems = items.stream()
                .filter(item -> item.getRarity() == nextRarity)
                .collect(Collectors.toList());

        for (double i = maxFloat - step; i > minFloat; i -= step) {
            ContractItem requiredItem;
            Contract contract;

            if (i == 0.45 || i == 0.38 || i == 0.15 || i == 0.07 || i == 0) {
                continue;
            }

            List<ContractItem> resultItems = new ArrayList<>();
            Contract lastContract = contracts.stream()
                    .reduce((first, second) -> second)
                    .orElse(null);
            Optional<ContractItem> optionalRequiredItem = contractItemService
                    .getContractItemWithLowestPriceByRarityAndFloat(items, rarity, i, includeStatTrak, tradeMarket);

            if (optionalRequiredItem.isPresent()){
                requiredItem = optionalRequiredItem.get();
            } else {
                log.warn("Lowest item not found. [Collection = {}, rarity = {}, quality = {}]", collection.getName(),
                        rarity.getName(), i);
                continue;
            }

            List<ContractItem> requiredItems = new ArrayList<>(10);

            for (int j = 0; j < CONTRACT_REQUIRED_ITEMS_COUNT; j++) {
                requiredItems.add(requiredItem);
            }

            for (Item item : nextRarityItems) {
                double resultFloat = calculateResultFloat(item.getMinFloat(), item.getMaxFloat(), i);
                ItemQuality quality = itemService.getQualityByFloat(resultFloat);
                Optional<ContractItem> contractItemOptional = contractItemService.map(item, quality, includeStatTrak, tradeMarket);
                contractItemOptional.ifPresent(resultItems::add);
            }

            BigDecimal contractPrice = requiredItem.getPrice().multiply(new BigDecimal(CONTRACT_REQUIRED_ITEMS_COUNT));
            double profitability = calculateProfitability(contractPrice, resultItems, tradeMarket.getCommission());
            contract = new Contract(contractPrice, i, i, collection, requiredItems, resultItems, profitability);

            if (contracts.size() == 0 ||  !(lastContract.getRequiredItems().equals(requiredItems) &&
                    lastContract.getResultItems().equals(resultItems))) {
                contracts.add(contract);
            } else {
                lastContract.setMinFloat(lastContract.getMinFloat() - step);
            }
        }
        return contracts;
    }
}
