package kz.aaslnv.csgo.easycontracts.price.model;

import kz.aaslnv.csgo.easycontracts.enumiration.ItemQuality;
import kz.aaslnv.csgo.easycontracts.item.model.Item;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "item_price")
public class ItemPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false, updatable = false)
    private Item item;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private ItemQuality quality;

    @Column(name = "is_stat_trak", nullable = false)
    private boolean statTrak;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;
}
