package kz.aaslnv.csgo.easycontracts.contract.model;

import kz.aaslnv.csgo.easycontracts.collection.model.Collection;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class Contract {

    private BigDecimal price;
    private double minFloat;
    private double maxFloat;
    private Collection collection;
    private List<ContractItem> requiredItems;
    private List<ContractItem> resultItems;
    private double profitability;
}
