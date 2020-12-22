package kz.aaslnv.csgo.easycontracts.enumiration;

import kz.aaslnv.csgo.easycontracts.item.model.ItemQuality;
import kz.aaslnv.csgo.easycontracts.item.service.ItemService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ItemQualityTest {

    @InjectMocks
    private ItemService itemService;

    @Test
    public void test_getQualitiesBetweenFloats(){
        double minFloat1 = 0, maxFloat1 = 1;
        double minFloat2 = 0, maxFloat2 = 0.5;
        double minFloat3 = 0.10, maxFloat3 = 0.4;
        double minFloat4 = 0.5, maxFloat4 = 0.9;
        double minFloat5 = 0.07, maxFloat5 = 0.4;
        double minFloat6 = 0.07, maxFloat6 = 0.15;

        List<ItemQuality> preparedResult1 = Arrays.asList(ItemQuality.values());
        List<ItemQuality> preparedResult2 = Arrays.asList(ItemQuality.values());
        List<ItemQuality> preparedResult3 = Arrays.asList(ItemQuality.MW, ItemQuality.FT, ItemQuality.WW);
        List<ItemQuality> preparedResult4 = Collections.singletonList(ItemQuality.BS);
        List<ItemQuality> preparedResult5 = Arrays.asList(ItemQuality.MW, ItemQuality.FT, ItemQuality.WW);
        List<ItemQuality> preparedResult6 = Collections.singletonList(ItemQuality.MW);

        List<ItemQuality> result1 = itemService.getQualitiesBetweenFloats(minFloat1, maxFloat1);
        List<ItemQuality> result2 = itemService.getQualitiesBetweenFloats(minFloat2, maxFloat2);
        List<ItemQuality> result3 = itemService.getQualitiesBetweenFloats(minFloat3, maxFloat3);
        List<ItemQuality> result4 = itemService.getQualitiesBetweenFloats(minFloat4, maxFloat4);
        List<ItemQuality> result5 = itemService.getQualitiesBetweenFloats(minFloat5, maxFloat5);
        List<ItemQuality> result6 = itemService.getQualitiesBetweenFloats(minFloat6, maxFloat6);

        Assert.assertEquals(preparedResult1, result1);
        Assert.assertEquals(preparedResult2, result2);
        Assert.assertEquals(preparedResult3, result3);
        Assert.assertEquals(preparedResult4, result4);
        Assert.assertEquals(preparedResult5, result5);
        Assert.assertEquals(preparedResult6, result6);
    }
}
