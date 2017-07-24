package com.gua.stock.crawl.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gua.open.connection.parameter.RequestParams;
import com.gua.open.connection.result.CommunicateResult;
import com.gua.open.connection.service.HttpCommunicateService;
import com.gua.open.jdbc.dto.StockDto;
import com.gua.open.jdbc.jdbctemplate.StockDao;
import com.gua.stock.crawl.service.CrawlStockService;
import com.gua.stock.crawl.service.StockCrawlerParamBuilder;
import com.gua.stock.crawl.service.StockInfoParser;

/**
 * 类CrawlStockServiceImpl.java的实现描述：TODO 类实现描述 
 * @author weicheng.lwc 2017年6月12日 下午5:37:08
 */
@Service("crawlStockService")
public class CrawlStockServiceImpl implements CrawlStockService {
    
    @Autowired
    private StockCrawlerParamBuilder stockCrawlerParamBuilder;

    @Autowired
    private HttpCommunicateService   httpCommunicateService;

    @Autowired
    private StockInfoParser          stockInfoParser;
    
    @Override
    public List<StockDto> crawl(Integer pageIndex) {
       RequestParams requestParams = buildParams(pageIndex);
       CommunicateResult result = communicate(requestParams);
       List<StockDto> stockDtoList = parseResult(result);
       return stockDtoList == null ? new ArrayList<StockDto>() : stockDtoList;
    }
    
    private RequestParams buildParams(Integer pageIndex) {
        return stockCrawlerParamBuilder.build(pageIndex);
    }
    
    private CommunicateResult communicate(RequestParams requestParams) {
        return httpCommunicateService.communicate(requestParams);
    }
    
    private List<StockDto> parseResult(CommunicateResult result) {
        if (null == result) {
            return new ArrayList<StockDto>();
        }
        
        return stockInfoParser.parseStockDtoList(result.getData());
    }
//    @Override
//    public List<StockDto> crawl(Integer pageIndex) {
//        List<StockDto> stockDtoList = new ArrayList<StockDto>();
//        RequestParams requestParams = stockCrawlerParamBuilder.build(pageIndex);
//        CommunicateResult result = httpCommunicateService.communicate(requestParams);
//        if (null != result) {
//            stockDtoList = stockInfoParser.parseStockDtoList(result.getData());
//        }
//        return stockDtoList;
//        
////        stockDao.batchInsert(stockDtoList);
//    }
}
