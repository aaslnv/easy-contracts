package kz.aaslnv.csgo.easycontracts.item.service;

import kz.aaslnv.csgo.easycontracts.enumiration.ItemRarity;
import kz.aaslnv.csgo.easycontracts.item.model.Item;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class ItemService {

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
