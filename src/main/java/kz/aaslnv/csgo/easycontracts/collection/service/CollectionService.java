package kz.aaslnv.csgo.easycontracts.collection.service;

import kz.aaslnv.csgo.easycontracts.collection.model.Collection;
import kz.aaslnv.csgo.easycontracts.collection.repository.CollectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CollectionService {

    private final CollectionRepository collectionRepository;

    public CollectionService(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    public List<Collection> getAll(){
        return collectionRepository.findAllByDeletedIsFalseAndTradableIsTrue();
    }

    public List<Collection> saveAll(List<Collection> collections){
        collections = collections.stream()
                .filter(collection -> !collectionRepository.findByName(collection.getName()).isPresent())
                .collect(Collectors.toList());

        return collectionRepository.saveAll(collections);
    }
}
