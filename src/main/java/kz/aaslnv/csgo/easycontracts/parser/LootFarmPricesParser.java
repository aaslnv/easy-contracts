package kz.aaslnv.csgo.easycontracts.parser;

import kz.aaslnv.csgo.easycontracts.item.model.ItemQuality;
import kz.aaslnv.csgo.easycontracts.enumiration.TradeMarket;
import kz.aaslnv.csgo.easycontracts.item.model.Item;
import kz.aaslnv.csgo.easycontracts.item.service.ItemService;
import kz.aaslnv.csgo.easycontracts.price.model.ItemPrice;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static kz.aaslnv.csgo.easycontracts.util.Constant.STAT_TRAK_PREFIX;

@Slf4j
@Component("LOOT_FARM")
public class LootFarmPricesParser implements IParser<ItemPrice> {

    public static final String BASE_URL = "https://loot.farm/fullprice.json";

    @Value("${application.contract.include_stat_trak}")
    private boolean includeStatTrak;

    private final ItemService itemService;

    private final OkHttpClient httpClient;

    @Autowired
    public LootFarmPricesParser(ItemService itemService, OkHttpClient httpClient) {
        this.itemService = itemService;
        this.httpClient = httpClient;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public List<ItemPrice> parse() {
        List<ItemPrice> prices = new ArrayList<>();
        Request request = new Request.Builder()
                .url(BASE_URL)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()){
                throw new RuntimeException("Unexpected code " + response);
            }

            if (response.body() == null){
                throw new RuntimeException("Response body is null");
            }

            JSONArray dataArray = new JSONArray(response.body().string());
            itemService.getAll().forEach(item -> prices.addAll(processParsePrices(item, dataArray)));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return prices;
    }

    private List<ItemPrice> processParsePrices(Item item, JSONArray dataArray){
        List<ItemPrice> itemPrices = new ArrayList<>();
        List<ItemQuality> itemQualities = itemService.getQualitiesBetweenFloats(item.getMinFloat(), item.getMaxFloat());

        itemQualities.forEach(quality -> {
            String itemFullName = item.getName() + " " + quality.getStyledName();
            String statTrakItemFullName = STAT_TRAK_PREFIX + " " + itemFullName;

            for (int i = 0; i < dataArray.length(); i++) {
                ItemPrice itemPrice;
                JSONObject jsonObject = dataArray.getJSONObject(i);
                String marketHashName = jsonObject.getString("name");
                double price = Double.parseDouble(jsonObject.optString("price", "0")) / 100d;

                if (Objects.equals(itemFullName, marketHashName)){
                    itemPrice = new ItemPrice(item, TradeMarket.LOOT_FARM, new BigDecimal(price), quality,
                            null, null, false);
                    itemPrices.add(itemPrice);
                } else if (includeStatTrak && Objects.equals(statTrakItemFullName, marketHashName)){
                    itemPrice = new ItemPrice(item, TradeMarket.LOOT_FARM, new BigDecimal(price), quality,
                            null, null, false);
                    itemPrices.add(itemPrice);
                }
            }
        });
        return itemPrices;
    }
}
