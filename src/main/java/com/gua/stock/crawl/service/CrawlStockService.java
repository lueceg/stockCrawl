package com.gua.stock.crawl.service;

import java.util.List;

import com.gua.open.jdbc.dto.StockDto;

/**
 * 类CrawlStockService.java的实现描述：TODO 类实现描述
 * 
 * @author weicheng.lwc 2017年6月11日 下午3:42:29
 */
public interface CrawlStockService {

    /**
     * 爬取Stock数据
     * 
     * @param pageIndex 第几页的数据
     */
    public List<StockDto> crawl(Integer pageIndex);

}
