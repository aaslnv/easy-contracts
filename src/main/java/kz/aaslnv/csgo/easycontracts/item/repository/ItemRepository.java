package kz.aaslnv.csgo.easycontracts.item.repository;

import kz.aaslnv.csgo.easycontracts.item.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByCollectionIdAndDeletedIsFalseAndStatTrak(Long collectionId, boolean isStatTrak);
}
