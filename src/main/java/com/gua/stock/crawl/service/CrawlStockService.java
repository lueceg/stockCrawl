package com.gua.stock.crawl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gua.open.connection.parameter.RequestParams;
import com.gua.open.connection.result.CommunicateResult;
import com.gua.open.connection.service.HttpCommunicateService;
import com.gua.open.jdbc.dto.StockDto;
import com.gua.open.jdbc.jdbctemplate.StockDao;

/**
 * 类CrawlStockService.java的实现描述：TODO 类实现描述
 * 
 * @author weicheng.lwc 2017年6月11日 下午3:42:29
 */
@Service("crawlStockService")
public class CrawlStockService {

    @Autowired
    private StockCrawlerParamBuilder stockCrawlerParamBuilder;

    @Autowired
    private HttpCommunicateService   httpCommunicateService;

    @Autowired
    private StockInfoParser          stockInfoParser;

    @Autowired
    private StockDao                 stockDao;

    public void crawl() {
        RequestParams requestParams = stockCrawlerParamBuilder.build(1);
        CommunicateResult result = httpCommunicateService.communicate(requestParams);
        List<StockDto> stockDtoList = stockInfoParser.parseStockDtoList(result.getData());
        stockDao.batchInsert(stockDtoList);

    }

}
