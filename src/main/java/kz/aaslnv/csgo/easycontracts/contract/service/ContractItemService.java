package kz.aaslnv.csgo.easycontracts.contract.service;

import kz.aaslnv.csgo.easycontracts.calculator.Calculator;
import kz.aaslnv.csgo.easycontracts.contract.model.ContractItem;
import kz.aaslnv.csgo.easycontracts.enumiration.ItemQuality;
import kz.aaslnv.csgo.easycontracts.enumiration.ItemRarity;
import kz.aaslnv.csgo.easycontracts.item.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ContractItemService {

    private final Calculator calculator;

    @Autowired
    public ContractItemService(Calculator calculator) {
        this.calculator = calculator;
    }

    public ContractItem getContractItemWithLowestPriceByRarityAndFloat(List<Item> items, ItemRarity rarity,
                                                                       double itemFloat, boolean isStatTrak){
        ItemQuality quality = calculator.getQualityByFloat(itemFloat);

        return items.stream()
                .filter(item -> item.getRarity() == rarity)
                .map(item -> map(item, quality, isStatTrak).orElse(null))
                .filter(item -> Objects.nonNull(item) && item.getPrice().intValue() != 0)
                .min(Comparator.comparing(item -> item.getPrice().doubleValue()))
                .orElseThrow(() -> new RuntimeException("Item with lowest price not found"));
    }

    public Optional<ContractItem> map(Item item, ItemQuality quality, boolean isStatTrak){
        String name = item.getName();
        ItemRarity rarity = item.getRarity();
        return item.getPrices().stream()
                .filter(itemPrice -> itemPrice.getQuality() == quality && itemPrice.isStatTrak() == isStatTrak)
                .findFirst()
                .map(itemPrice -> new ContractItem(name, quality, rarity, itemPrice.getPrice()));
    }

}
