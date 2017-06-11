package com.gua.stock.crawl;

import com.gua.stock.crawl.service.CrawlStockService;

/**
 * 类CrawlStockTask.java的实现描述：TODO 类实现描述 
 * @author weicheng.lwc 2017年6月11日 下午3:40:10
 */
public class CrawlStockTask implements Runnable {
    
    private CrawlStockService crawlStockService;
    
    @Override
    public void run() {
        crawlStockService.crawl();
    }
    

}
