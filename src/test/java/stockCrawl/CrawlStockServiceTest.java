package stockCrawl;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.gua.stock.crawl.service.CrawlStockService;

/**
 * 类CrawlStockServiceTest.java的实现描述：TODO 类实现描述
 * 
 * @author weicheng.lwc 2017年6月11日 下午6:55:20
 */
public class CrawlStockServiceTest {

    private static ApplicationContext context = new ClassPathXmlApplicationContext("service-beans.xml");

    @Test
    public void testCrawlStockService() {
        CrawlStockService crawlStockService = context.getBean(CrawlStockService.class);
        crawlStockService.crawl();
    }

}
