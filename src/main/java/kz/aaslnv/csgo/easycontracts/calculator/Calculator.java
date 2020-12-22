package kz.aaslnv.csgo.easycontracts.calculator;

import kz.aaslnv.csgo.easycontracts.contract.model.ContractItem;
import kz.aaslnv.csgo.easycontracts.enumiration.ItemQuality;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class Calculator {

    @Value("${application.contract.tm_commission:0}")
    private double tradeMarketCommission;

    public ItemQuality getQualityByFloat(double itemFloat){
        if (itemFloat >= ItemQuality.FN.getMinFloat() && itemFloat < ItemQuality.FN.getMaxFloat()) {
            return ItemQuality.FN;
        } else if (itemFloat >= ItemQuality.MW.getMinFloat() && itemFloat < ItemQuality.MW.getMaxFloat()) {
            return ItemQuality.MW;
        } else if (itemFloat >= ItemQuality.FT.getMinFloat() && itemFloat < ItemQuality.FT.getMaxFloat()) {
            return ItemQuality.FT;
        } else if (itemFloat >= ItemQuality.WW.getMinFloat() && itemFloat < ItemQuality.WW.getMaxFloat()) {
            return ItemQuality.WW;
        } else {
            return ItemQuality.BS;
        }
    }

    public double calculateResultFloat(double minFloat, double maxFloat, double average){
        return  (maxFloat - minFloat) * average + minFloat;
    }

    public double calculateProfitability(BigDecimal contractPrice, List<ContractItem> resultItems){
        int resultItemCount = resultItems.size();
        BigDecimal sum = new BigDecimal(0);

        for (ContractItem item : resultItems) {
            sum = sum.add(item.getPrice());
        }

        double averageProfit = sum.doubleValue() / resultItemCount;

        return (averageProfit * (1 - tradeMarketCommission)) / contractPrice.doubleValue() - 1;
    }
}
