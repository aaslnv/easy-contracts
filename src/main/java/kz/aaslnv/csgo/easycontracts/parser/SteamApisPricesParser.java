package kz.aaslnv.csgo.easycontracts.parser;

import kz.aaslnv.csgo.easycontracts.enumiration.ItemQuality;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static kz.aaslnv.csgo.easycontracts.util.Constant.STAT_TRAK_PREFIX;

@Slf4j
@Service
public class SteamApisPricesParser implements IParser<ItemPrice> {

    public static final String BASE_URL = "http://api.steamapis.com/market/items/730";

    @Value("${application.api_keys.steamapis}")
    private String apiKey;

    @Value("${application.contract.include_stat_trak}")
    private boolean includeStatTrak;

    private final ItemService itemService;

    private final OkHttpClient httpClient;



    @Autowired
    public SteamApisPricesParser(ItemService itemService, OkHttpClient httpClient) {
        this.itemService = itemService;
        this.httpClient = httpClient;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public List<ItemPrice> parse() {
        List<ItemPrice> prices = new ArrayList<>();
        Request request = new Request.Builder()
                .url(BASE_URL + "?api_key=" + apiKey)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()){
                throw new RuntimeException("Unexpected code " + response);
            }

            if (response.body() == null){
                throw new RuntimeException("Response body is null");
            }

            JSONObject jsonObject = new JSONObject(response.body().string());
            JSONArray dataArray = jsonObject.getJSONArray("data");
            itemService.getAll().forEach(item -> prices.addAll(processParsePrices(item, dataArray)));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return prices;
    }

    private List<ItemPrice> processParsePrices(Item item, JSONArray dataArray){
        List<ItemPrice> itemPrices = new ArrayList<>();
        List<ItemQuality> itemQualities = ItemQuality.getQualitiesBetweenFloats(item.getMinFloat(), item.getMaxFloat());

        itemQualities.forEach(quality -> {
            String itemFullName = item.getName() + " " + quality.getStyledName();
            String statTrakItemFullName = STAT_TRAK_PREFIX + " " + itemFullName;

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject jsonObject = dataArray.getJSONObject(i);
                String marketHashName = jsonObject.getString("market_hash_name");
                JSONObject pricesObject = jsonObject.getJSONObject("prices");

                if (Objects.equals(itemFullName, marketHashName)){
                    itemPrices.add(createItemPrice(item, pricesObject, quality, false));
                } else if (includeStatTrak && Objects.equals(statTrakItemFullName, marketHashName)){
                    itemPrices.add(createItemPrice(item, pricesObject, quality, true));
                }
            }
        });
        return itemPrices;

    }

    private ItemPrice createItemPrice(Item item, JSONObject jsonObject, ItemQuality quality, boolean isStatTrak){
        double avgPrice = jsonObject.optDouble("avg", 0);
        Integer avgDailyVolume = null;
        Integer last24hoursSales = null;

        try {
            JSONObject salesObject = jsonObject.getJSONObject("sold");
            avgDailyVolume = salesObject.optInt("avg_daily_volume", 0);
            last24hoursSales = salesObject.optInt("last_24h", 0);
        } catch (Exception e){
            log.error(e.getMessage(), e);
        }

        return new ItemPrice(item, TradeMarket.STEAM, new BigDecimal(avgPrice), quality, last24hoursSales,
                avgDailyVolume, isStatTrak);
    }
}
