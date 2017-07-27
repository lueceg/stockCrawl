package com.gua.stock.crawl.service.impl;

import com.gua.open.connection.parameter.RequestParams;
import com.gua.open.connection.result.CommunicateResult;
import com.gua.open.connection.service.HttpCommunicateService;
import com.gua.open.mybatis.dto.StockDao;
import com.gua.stock.crawl.service.CrawlStockService;
import com.gua.stock.crawl.service.StockCrawlerParamBuilder;
import com.gua.stock.crawl.service.StockInfoParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public List<StockDao> crawl(Integer pageIndex) {
       RequestParams requestParams = buildParams(pageIndex);
       CommunicateResult result = communicate(requestParams);
       List<StockDao> stockDtoList = parseResult(result);
       return stockDtoList == null ? new ArrayList<StockDao>() : stockDtoList;
    }
    
    private RequestParams buildParams(Integer pageIndex) {
        return stockCrawlerParamBuilder.build(pageIndex);
    }
    
    private CommunicateResult communicate(RequestParams requestParams) {
        return httpCommunicateService.communicate(requestParams);
    }
    
    private List<StockDao> parseResult(CommunicateResult result) {
        if (null == result) {
            return new ArrayList<StockDao>();
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
