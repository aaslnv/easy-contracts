package kz.aaslnv.csgo.easycontracts.price.service;

import kz.aaslnv.csgo.easycontracts.enumiration.TradeMarket;
import kz.aaslnv.csgo.easycontracts.price.model.ItemPrice;
import kz.aaslnv.csgo.easycontracts.price.repository.ItemPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ItemPriceService {

    private final ItemPriceRepository itemPriceRepository;

    @Autowired
    public ItemPriceService(ItemPriceRepository itemPriceRepository) {
        this.itemPriceRepository = itemPriceRepository;
    }

    public void saveAll(List<ItemPrice> prices){
        prices.forEach(price -> {
            Optional<ItemPrice> optionalItemPrice = itemPriceRepository
                    .findByItemIdAndQualityAndTradeMarketAndStatTrak(price.getItem().getId(), price.getQuality(),
                            price.getTradeMarket(), price.isStatTrak());

            if (optionalItemPrice.isPresent()){
                ItemPrice dbItemPrice = optionalItemPrice.get();
                price.setId(dbItemPrice.getId());
                price.setDeleted(dbItemPrice.isDeleted());
            }

            itemPriceRepository.save(price);
        });
    }

    public void deleteAllByTradeMarket(TradeMarket tradeMarket){
        System.out.println(tradeMarket);
        itemPriceRepository.deleteAllByTradeMarket(tradeMarket);
    }
}
