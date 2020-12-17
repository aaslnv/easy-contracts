package kz.aaslnv.csgo.easycontracts.enumiration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum ItemRarity {

    CONSUMER("Consumer",1, false),
    INDUSTRIAL("Industrial",2, true),
    MIL_SPEC("Mil-Spec",3, true),
    RESTRICTED("Restricted",4, true),
    CLASSIFIED("Classified",5, true),
    COVERT("Covert",6, true),
    CONTRABAND("Contraband",7, false);

    private final String name;
    private final int priority;
    private final boolean canBeTraded;

    public static Optional<ItemRarity> getRarityByPriority(int priority) {
        return Arrays.stream(ItemRarity.values())
                .filter(rarity -> rarity.getPriority() == priority)
                .findFirst();
    }

    public static Optional<ItemRarity> getRarityByName(String name) {
        if (Objects.isNull(name) || name.isEmpty()){
            return Optional.empty();
        }

        return Arrays.stream(ItemRarity.values())
                .filter(rarity -> name.toLowerCase().contains(rarity.getName().toLowerCase()))
                .findFirst();
    }
}
