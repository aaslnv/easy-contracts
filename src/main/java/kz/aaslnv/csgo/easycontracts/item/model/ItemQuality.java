package kz.aaslnv.csgo.easycontracts.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum ItemQuality {

    FN("Factory New", 0, 0.07),
    MW("Minimal Wear", 0.07, 0.15),
    FT("Field-Tested", 0.15, 0.38),
    WW("Well-Worn", 0.38, 0.45),
    BS("Battle-Scarred", 0.45, 1);

    private final String name;
    private final double minFloat;
    private final double maxFloat;

    public String getStyledName(){
        return "(" + name + ")";
    }
}
