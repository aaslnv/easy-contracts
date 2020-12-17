package kz.aaslnv.csgo.easycontracts.item.service;

import kz.aaslnv.csgo.easycontracts.collection.model.Collection;
import kz.aaslnv.csgo.easycontracts.enumiration.ItemQuality;
import kz.aaslnv.csgo.easycontracts.enumiration.ItemRarity;
import kz.aaslnv.csgo.easycontracts.item.model.Item;
import kz.aaslnv.csgo.easycontracts.item.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public double getMinFloatByItemsAndRarity(List<Item> items, ItemRarity rarity){
        return items.stream()
                .filter(item -> item.getRarity() == rarity)
                .min(Comparator.comparing(Item::getMinFloat))
                .map(Item::getMinFloat)
                .orElseThrow(() -> new RuntimeException("Min float not found"));
    }

    public double getMaxFloatByItemsAndRarity(List<Item> items, ItemRarity rarity){
        return items.stream()
                .filter(item -> item.getRarity() == rarity)
                .max(Comparator.comparing(Item::getMinFloat))
                .map(Item::getMinFloat)
                .orElseThrow(() -> new RuntimeException("Max float not found"));
    }
}
