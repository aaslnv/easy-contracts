package kz.aaslnv.csgo.easycontracts.price.model;

import kz.aaslnv.csgo.easycontracts.enumiration.ItemQuality;
import kz.aaslnv.csgo.easycontracts.enumiration.TradeMarket;
import kz.aaslnv.csgo.easycontracts.item.model.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "item_price")
@NoArgsConstructor
@AllArgsConstructor
public class ItemPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false, updatable = false)
    private Item item;

    @Enumerated(EnumType.STRING)
    @Column(name = "trade_market", nullable = false, updatable = false)
    private TradeMarket tradeMarket;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private ItemQuality quality;

    @Column(name = "last_24_hours_sales")
    private Integer last24hoursSales;

    @Column(name = "avg_daily_volume")
    private Integer avgDailyVolume;

    @Column(name = "is_stat_trak", nullable = false)
    private boolean statTrak;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;

    public ItemPrice(Item item, TradeMarket tradeMarket, BigDecimal price, ItemQuality quality,
                     Integer last24hoursSales, Integer avgDailyVolume, boolean statTrak) {
        this.item = item;
        this.tradeMarket = tradeMarket;
        this.price = price;
        this.quality = quality;
        this.last24hoursSales = last24hoursSales;
        this.avgDailyVolume = avgDailyVolume;
        this.statTrak = statTrak;
    }
}
