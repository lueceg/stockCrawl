package com.gua.stock.crawl.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gua.open.jdbc.dto.StockDto;

/**
 * 类StockInfoParserImpl.java的实现描述：TODO 类实现描述
 * 
 * @author weicheng.lwc 2017年4月10日 下午7:44:51
 */
@Service("stockInfoParser")
public class StockInfoParserImpl implements StockInfoParser {
    
    private static final Logger logger = LoggerFactory.getLogger(StockInfoParserImpl.class);
    
    private static final String BEGIN_MARK = "[\"";
    
    private static final String END_MARK = "\"]";
    
    private static final String SPLIT_MARK = "\",\"";
    
    /**
     * 
     * @param data data中的部分数据有类似数据【从事"周大生"品牌】，其中的【"】会影响json的解析，故不适用json进行处理
     * @return
     */
    @Override
    public List<StockDto> parseStockDtoList(String data) {
        if (null == data) {
            return null;
        }
        
        int begin = data.indexOf(BEGIN_MARK);
        int end = data.lastIndexOf(END_MARK);
        
        if (begin>=0 && end >0) {
            String validData = data.substring(begin + BEGIN_MARK.length(), end);
            if (StringUtils.isNoneBlank(validData)) {
                List<StockDto> stockDtoList = new ArrayList<StockDto>();
                String[] stockDatas = validData.split(SPLIT_MARK);
                for (String stockData : stockDatas) {
                    List<String> stockDetails = Arrays.asList(stockData.split(","));
                    StockDto stockDto = new StockDto();
                    stockDto.setStockCode(str2Integer(stockDetails.get(4)));
                    stockDto.setStockName(stockDetails.get(3));
                    stockDto.setContinuousBlankPlateQuantity(stockDetails.get(28));
                    stockDto.setTotalIncrease(str2Float(stockDetails.get(29)));
//                    stockDto.setSignProfit(str2Float(stockDetails));
                    stockDto.setCrawlDate(new Date(System.currentTimeMillis()));
                    
                    stockDtoList.add(stockDto);
                }
                return stockDtoList;
            }
        }
        
        return null;
    }
    
    @Override
    public List<List<String>> parseSimple(String data) {
        if (null == data) {
            return null;
        }
        
        JSONArray jsonArray = JSON.parseArray(data);
        List<List<String>>  stockList = new ArrayList<List<String>>();
        for (String stock : jsonArray.toJavaList(String.class)) {
            if (StringUtils.isBlank(stock)) {
                continue;
            }
            String[] stockDetails = stock.split(",");
            List<String> stockDetailList = new ArrayList<String>();
            for (String stockDetail : stockDetails) {
                stockDetailList.add(stockDetail);
            }
            stockList.add(stockDetailList);
        }
        
        return stockList;
    }
    
    private Integer str2Integer(String str) {
        if (StringUtils.isBlank(str)) {
            return Integer.valueOf(0);
        }
        
        Integer value = Integer.valueOf(0);
        try {
            value = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
        }
        
        return value;
    }
    
    private Float str2Float(String str) {
        if (StringUtils.isBlank(str)) {
            return Float.valueOf(0);
        }
        
        Float value = Float.valueOf(0);
        try {
            value = Float.parseFloat(str);
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
        }
        
        return value;
    }

}
