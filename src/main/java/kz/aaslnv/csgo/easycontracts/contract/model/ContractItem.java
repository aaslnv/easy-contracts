package kz.aaslnv.csgo.easycontracts.contract.model;

import kz.aaslnv.csgo.easycontracts.enumiration.ItemQuality;
import kz.aaslnv.csgo.easycontracts.enumiration.ItemRarity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ContractItem {

    private String name;
    private ItemQuality quality;
    private ItemRarity rarity;
    private BigDecimal price;
}
