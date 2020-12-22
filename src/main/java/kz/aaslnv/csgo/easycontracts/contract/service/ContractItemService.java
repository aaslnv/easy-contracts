package kz.aaslnv.csgo.easycontracts.contract.service;

import kz.aaslnv.csgo.easycontracts.contract.model.ContractItem;
import kz.aaslnv.csgo.easycontracts.enumiration.TradeMarket;
import kz.aaslnv.csgo.easycontracts.item.model.ItemQuality;
import kz.aaslnv.csgo.easycontracts.item.model.ItemRarity;
import kz.aaslnv.csgo.easycontracts.item.model.Item;
import kz.aaslnv.csgo.easycontracts.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ContractItemService {

    private final ItemService itemService;

    @Autowired
    public ContractItemService(ItemService itemService) {
        this.itemService = itemService;
    }

    public Optional<ContractItem> getContractItemWithLowestPriceByRarityAndFloat(List<Item> items, ItemRarity rarity,
                                                                                 double itemFloat, boolean isStatTrak,
                                                                                 TradeMarket tradeMarket){
        ItemQuality quality = itemService.getQualityByFloat(itemFloat);

        return items.stream()
                .filter(item -> item.getRarity() == rarity)
                .map(item -> map(item, quality, isStatTrak, tradeMarket).orElse(null))
                .filter(item -> Objects.nonNull(item) && item.getPrice().doubleValue() != 0)
                .min(Comparator.comparing(item -> item.getPrice().doubleValue()));
    }

    public Optional<ContractItem> map(Item item, ItemQuality quality, boolean isStatTrak, TradeMarket tradeMarket){
        return item.getPrices().stream()
                .filter(itemPrice -> itemPrice.getQuality() == quality && itemPrice.isStatTrak() == isStatTrak &&
                        itemPrice.getTradeMarket() == tradeMarket)
                .findFirst()
                .map(itemPrice -> new ContractItem(item.getName(), quality, item.getRarity(), itemPrice.getPrice()));
    }

}
