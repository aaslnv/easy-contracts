package kz.aaslnv.csgo.easycontracts.enumiration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TradeMarket {

    CS_MONEY(0),
    STEAM(0),
    LOOT_FARM(0),
    BITSKINS(0),
    BITSKINS_IS(0),
    CSGO_TM(0);

    private final double commission;
}
