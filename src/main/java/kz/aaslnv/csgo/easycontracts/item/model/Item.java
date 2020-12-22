package kz.aaslnv.csgo.easycontracts.item.model;

import kz.aaslnv.csgo.easycontracts.collection.model.Collection;
import kz.aaslnv.csgo.easycontracts.price.model.ItemPrice;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "item")
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String name;

    @Column(name = "min_float", nullable = false, updatable = false)
    private double minFloat;

    @Column(name = "max_float", nullable = false, updatable = false)
    private double maxFloat;

    @ManyToOne
    @JoinColumn(name = "collection_id", nullable = false, updatable = false)
    private Collection collection;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private ItemRarity rarity;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ItemPrice> prices;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;

    public Item(String name, double minFloat, double maxFloat, Collection collection, ItemRarity rarity) {
        this.name = name;
        this.minFloat = minFloat;
        this.maxFloat = maxFloat;
        this.collection = collection;
        this.rarity = rarity;
    }
}
