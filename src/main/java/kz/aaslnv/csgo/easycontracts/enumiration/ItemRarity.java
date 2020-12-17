package kz.aaslnv.csgo.easycontracts.enumiration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemRarity {

    CONSUMER(1, false),
    INDUSTRIAL(2, true),
    MIL_SPEC(3, true),
    RESTRICTED(4, true),
    CLASSIFIED(5, true),
    COVERT(6, true),
    CONTRABAND(7, false);

    private final int priority;
    private final boolean canBeTraded;

    public static ItemRarity getRarityByPriority(int priority){
        switch (priority) {
            case 1: return CONSUMER;
            case 2: return INDUSTRIAL;
            case 3: return MIL_SPEC;
            case 4: return RESTRICTED;
            case 5: return CLASSIFIED;
            case 6: return COVERT;
            default: return CONTRABAND;
        }
    }
}
