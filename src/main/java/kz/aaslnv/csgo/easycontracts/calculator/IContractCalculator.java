package kz.aaslnv.csgo.easycontracts.calculator;

import kz.aaslnv.csgo.easycontracts.contract.model.Contract;

import java.util.List;

public interface IContractCalculator {

    List<Contract> calculate(boolean isStatTrak);
}
