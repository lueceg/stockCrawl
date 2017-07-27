package com.gua.stock.crawl;

import com.gua.open.mybatis.dto.StockDao;
import com.gua.stock.crawl.service.CrawlStockService;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * 类CrawlStockTask.java的实现描述：TODO 类实现描述
 * 
 * @author weicheng.lwc 2017年6月11日 下午3:40:10
 */
public class CrawlStockTask implements Callable<List<StockDao>> {

    private CrawlStockService crawlStockService;

    private Integer           pageIndex;

    public CrawlStockTask(CrawlStockService crawlStockService, Integer pageIndex){
        this.crawlStockService = crawlStockService;
        this.pageIndex = pageIndex;
    }

    @Override
    public List<StockDao> call() throws Exception {
        List<StockDao> stockDtoList = crawlStockService.crawl(pageIndex);
        return stockDtoList;
    }

}
