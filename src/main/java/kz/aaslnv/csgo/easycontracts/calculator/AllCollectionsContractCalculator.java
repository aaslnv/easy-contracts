package kz.aaslnv.csgo.easycontracts.calculator;

import kz.aaslnv.csgo.easycontracts.contract.model.Contract;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component(value = "all")
public class AllCollectionsContractCalculator implements IContractCalculator {

    @Override
    public List<Contract> calculate() {
        return new ArrayList<>();
    }
}
