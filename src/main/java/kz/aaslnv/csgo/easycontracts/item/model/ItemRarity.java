package kz.aaslnv.csgo.easycontracts.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum ItemRarity {

    CONSUMER("Consumer",1, false, true),
    INDUSTRIAL("Industrial",2, true, true),
    MIL_SPEC("Mil-Spec",3, true, true),
    RESTRICTED("Restricted",4, true, true),
    CLASSIFIED("Classified",5, true, true),
    COVERT("Covert",6, true, false),
    CONTRABAND("Contraband",7, false, false);

    private final String name;
    private final int priority;
    private final boolean canBeTraded;
    private final boolean tradable;
}
