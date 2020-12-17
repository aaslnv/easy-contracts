package kz.aaslnv.csgo.easycontracts.calculator;

import kz.aaslnv.csgo.easycontracts.contract.model.Contract;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AllCollectionsContractCalculator implements IContractCalculator {

    @Override
    public List<Contract> calculate(boolean isStatTrak) {
        return new ArrayList<>();
    }
}
