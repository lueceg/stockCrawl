package com.gua.stock.crawl;

import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;

import com.gua.open.jdbc.dto.StockDto;
import com.gua.open.jdbc.jdbctemplate.StockDao;
import com.gua.stock.crawl.service.CrawlStockService;

/**
 * 类CrawlStockTask.java的实现描述：TODO 类实现描述
 * 
 * @author weicheng.lwc 2017年6月11日 下午3:40:10
 */
public class CrawlStockTask implements Callable<List<StockDto>> {

    private CrawlStockService crawlStockService;

    private Integer           pageIndex;

    public CrawlStockTask(CrawlStockService crawlStockService, Integer pageIndex){
        this.crawlStockService = crawlStockService;
        this.pageIndex = pageIndex;
    }

    @Override
    public List<StockDto> call() throws Exception {
        List<StockDto> stockDtoList = crawlStockService.crawl(pageIndex);
        return stockDtoList;
    }

}
