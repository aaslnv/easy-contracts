package kz.aaslnv.csgo.easycontracts.calculator;

import kz.aaslnv.csgo.easycontracts.contract.model.Contract;
import kz.aaslnv.csgo.easycontracts.contract.model.ContractItem;

import java.math.BigDecimal;
import java.util.List;

public interface IContractCalculator {

    List<Contract> calculate();

    default double calculateResultFloat(double minFloat, double maxFloat, double average){
        return (maxFloat - minFloat) * average + minFloat;
    }

    default double calculateProfitability(BigDecimal contractPrice, List<ContractItem> resultItems, double tradeMarketCommission){
        int resultItemCount = resultItems.size();
        BigDecimal sum = new BigDecimal(0);

        for (ContractItem item : resultItems) {
            sum = sum.add(item.getPrice());
        }

        double averageProfit = sum.doubleValue() / resultItemCount;
        return (averageProfit * (1 - tradeMarketCommission)) / contractPrice.doubleValue() - 1;
    }
}
