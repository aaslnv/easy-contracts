package kz.aaslnv.csgo.easycontracts.collection.repository;

import kz.aaslnv.csgo.easycontracts.collection.model.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    List<Collection> findAllByDeletedIsFalseAndTradableIsTrue();
}
