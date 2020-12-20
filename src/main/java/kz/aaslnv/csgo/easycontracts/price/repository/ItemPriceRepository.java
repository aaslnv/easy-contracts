package kz.aaslnv.csgo.easycontracts.price.repository;

import kz.aaslnv.csgo.easycontracts.enumiration.ItemQuality;
import kz.aaslnv.csgo.easycontracts.enumiration.TradeMarket;
import kz.aaslnv.csgo.easycontracts.price.model.ItemPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemPriceRepository extends JpaRepository<ItemPrice, Long> {

    Optional<ItemPrice> findByItemIdAndQualityAndTradeMarketAndStatTrak(Long itemId, ItemQuality quality,
                                                                        TradeMarket tradeMarket, boolean isStatTrak);

}
