package kz.aaslnv.csgo.easycontracts.collection.repository;

import kz.aaslnv.csgo.easycontracts.collection.model.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    List<Collection> findAllByDeletedIsFalseAndTradableIsTrue();

    Optional<Collection> findByName(String name);
}
