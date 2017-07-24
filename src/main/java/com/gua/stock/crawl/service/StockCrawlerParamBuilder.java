package com.gua.stock.crawl.service;

import com.gua.open.connection.parameter.RequestParams;

/**
 * 类StockCrawlerParamBuilder.java的实现描述：TODO 类实现描述
 * 
 * @author weicheng.lwc 2017年6月11日 下午4:10:58
 */
public interface StockCrawlerParamBuilder {

    public RequestParams build(Integer pageNum);

}
