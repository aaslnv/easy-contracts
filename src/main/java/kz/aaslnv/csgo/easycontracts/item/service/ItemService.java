package kz.aaslnv.csgo.easycontracts.item.service;

import kz.aaslnv.csgo.easycontracts.item.model.ItemQuality;
import kz.aaslnv.csgo.easycontracts.item.model.ItemRarity;
import kz.aaslnv.csgo.easycontracts.item.model.Item;
import kz.aaslnv.csgo.easycontracts.item.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> getAll(){
        return itemRepository.findAllByDeletedIsFalse();
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

    public Optional<ItemRarity> getRarityByPriority(int priority) {
        return Arrays.stream(ItemRarity.values())
                .filter(rarity -> rarity.getPriority() == priority)
                .findFirst();
    }

    public Optional<ItemRarity> getRarityByName(String name) {
        if (Objects.isNull(name) || name.isEmpty()){
            return Optional.empty();
        }

        return Arrays.stream(ItemRarity.values())
                .filter(rarity -> name.toLowerCase().contains(rarity.getName().toLowerCase()))
                .findFirst();
    }

    public ItemQuality getQualityByFloat(double itemFloat){
        if (itemFloat >= ItemQuality.FN.getMinFloat() && itemFloat < ItemQuality.FN.getMaxFloat()) {
            return ItemQuality.FN;
        } else if (itemFloat >= ItemQuality.MW.getMinFloat() && itemFloat < ItemQuality.MW.getMaxFloat()) {
            return ItemQuality.MW;
        } else if (itemFloat >= ItemQuality.FT.getMinFloat() && itemFloat < ItemQuality.FT.getMaxFloat()) {
            return ItemQuality.FT;
        } else if (itemFloat >= ItemQuality.WW.getMinFloat() && itemFloat < ItemQuality.WW.getMaxFloat()) {
            return ItemQuality.WW;
        } else {
            return ItemQuality.BS;
        }
    }

    public List<ItemQuality> getQualitiesBetweenFloats(double minFloat, double maxFloat){
        return Arrays.stream(ItemQuality.values())
                .filter(quality -> (quality.getMinFloat() < maxFloat && quality.getMaxFloat() > minFloat))
                .collect(Collectors.toList());
    }
}
