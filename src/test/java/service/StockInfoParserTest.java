package service;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;


/**
 * 类StockInfoParserTest.java的实现描述：TODO 类实现描述 
 * @author weicheng.lwc 2017年6月17日 下午2:53:35
 */
public class StockInfoParserTest {
    
//    private static final String data = "[",32450,80437106,永安行,603776,732776,24000000.00,9600000.00,9000.00,,,,,,,,,,,sh,1000.00,,90000.00,,,,http://topic.eastmoney.com/czyaggzxcIPO/,以公共自行车系统的研发、销售、建设、运营为核心业务‚旨在运用物联网技术在各市县构建绿色交通体系‚为广大民众提供绿色交通服务;发行人通过移动互联网、在线支付、大数据分析等技术手段增强用户体验、提高用户粘性‚推广和普及绿色出行理念‚实现社会价值和企业价值的和谐统一。,26.85,0.900000,24.170000,,0,,26.85,0.90,24.17,9.00,待上市,,,,,AN201704260533856134,22.99,53.90,,26.85,22.99,暂缓发行,26.85,22.99""

    private static final String data = "AN201203020004754885,3604,80103370,金隅股份,601992,,410404560.00,316008000.00,,,9.00,2011-02-01,,2011-03-01,20.43,,,,,sh,1000.00,,,6.04,15.05,,,水泥及预拌混凝土、新型建材与商贸物流、房地产开发、物业投资及管理,,,,,0,,,,,,,,,,,,,,,,,,,";

    @Test
    public void testParseStockDtoList() {
        String[] split = data.split(",", -1);
        System.out.println(split.length);
        List<String> list = Arrays.asList(data.split(","));
        System.out.println(list.size());
        System.out.println(list.get(list.size() -1 ));
    }

}
