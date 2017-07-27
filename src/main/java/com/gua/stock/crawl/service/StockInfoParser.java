package com.gua.stock.crawl.service;

import com.gua.open.mybatis.dto.StockDao;

import java.util.List;

/**
 * 类StockInfoParser.java的实现描述：TODO 类实现描述
 * 
 * @author weicheng.lwc 2017年4月10日 下午7:36:10
 */
public interface StockInfoParser {

    public List<StockDao> parseStockDtoList(String data);

    /**
     * 简单解析
     * 
     * @param data
     * @return
     */
    public List<List<String>> parseSimple(String data);
}
