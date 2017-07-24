package com.gua.stock.crawl.service;

import java.util.List;

import com.gua.open.jdbc.dto.StockDto;

/**
 * 类StockInfoParser.java的实现描述：TODO 类实现描述
 * 
 * @author weicheng.lwc 2017年4月10日 下午7:36:10
 */
public interface StockInfoParser {

    public List<StockDto> parseStockDtoList(String data);

    /**
     * 简单解析
     * 
     * @param data
     * @return
     */
    public List<List<String>> parseSimple(String data);
}
