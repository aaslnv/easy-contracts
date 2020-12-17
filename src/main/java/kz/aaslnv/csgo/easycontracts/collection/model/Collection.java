package kz.aaslnv.csgo.easycontracts.collection.model;

import kz.aaslnv.csgo.easycontracts.item.model.Item;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "collection")
public class Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Item> items;

    @Column(name = "is_tradable", nullable = false)
    private boolean tradable = true;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;
}
