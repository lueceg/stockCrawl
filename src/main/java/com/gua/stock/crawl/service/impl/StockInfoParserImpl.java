package com.gua.stock.crawl.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gua.open.mybatis.dto.StockDao;
import com.gua.stock.crawl.service.StockInfoParser;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 类StockInfoParserImpl.java的实现描述：TODO 类实现描述
 *
 * @author weicheng.lwc 2017年4月10日 下午7:44:51
 */
@Service("stockInfoParser")
public class StockInfoParserImpl implements StockInfoParser {

    private static final Logger logger = LoggerFactory.getLogger(StockInfoParserImpl.class);

    private static DateTimeFormatter dateFormatToDay    = DateTimeFormat.forPattern("yyyy-MM-dd");

    private static final String BEGIN_MARK = "[\"";

    private static final String END_MARK = "\"]";

    private static final String SPLIT_MARK = "\",\"";

    /**
     * @param data data中的部分数据有类似数据【从事"周大生"品牌】，其中的【"】会影响json的解析，故不适用json进行处理
     * @return
     */
    @Override
    public List<StockDao> parseStockDtoList(String data) {
        if (null == data) {
            return null;
        }

        int begin = data.indexOf(BEGIN_MARK);
        int end = data.lastIndexOf(END_MARK);

        if (begin >= 0 && end > 0) {
            String validData = data.substring(begin + BEGIN_MARK.length(), end);
            if (StringUtils.isNoneBlank(validData)) {
                List<StockDao> stockDtoList = new ArrayList<StockDao>();
                String[] stockDatas = validData.split(SPLIT_MARK);
                for (String stockData : stockDatas) {
                    List<String> stockDetails = Arrays.asList(stockData.split(",", -1)); // 用","分割需要使用尽可能多次
                    StockDao stockDto = generateStockDto(stockDetails);
                    if (null != stockData) {
                        stockDtoList.add(stockDto);
                    }
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
        List<List<String>> stockList = new ArrayList<List<String>>();
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

    private StockDao generateStockDto(List<String> stockDetails) {
        if (CollectionUtils.isEmpty(stockDetails)) {
            return null;
        }

        if (stockDetails.size() <= 40) {
            logger.error(stockDetails.toString());
            return null;
        }

        Integer stockCode = str2Integer(stockDetails.get(4));
        String stockName = stockDetails.get(3);
        String mkt = StringUtils.equals("sh", stockDetails.get(19)) ? "sh" : "sz";


        StockDao stockDto = new StockDao();
        stockDto.setStockCode(stockCode);
        stockDto.setStockName(stockName);

        String continuousBlankPlateQuantity = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(stockDetails.get(40))) {
            StringBuffer sb  = new StringBuffer(stockDetails.get(40));
            sb.append("(http://quote.eastmoney.com/");
            sb.append(mkt);
            sb.append(stockCode);
            sb.append(".html)");
            continuousBlankPlateQuantity = sb.toString();
        } else {
            continuousBlankPlateQuantity = stockDetails.get(38);
        }
        stockDto.setContinuousBlankPlateQuantity(continuousBlankPlateQuantity);

        float fZxj = str2Float(stockDetails.get(23));
        float fFxj = str2Float(stockDetails.get(10));
        float totalIncrease = 0F;
        if ("未开板".equals(stockDetails.get(38)) && Float.compare(fZxj, Float.MAX_VALUE) != 0 && Float.compare(fFxj, Float.MAX_VALUE) != 0 && Float.compare(fFxj, 0.0F) != 0) {
            float preciseTotalIncrease = (fZxj - fFxj) / fFxj * 100;
            BigDecimal bigDecimal = new BigDecimal(preciseTotalIncrease);
            totalIncrease = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        } else {
            float totalIncreaseTemp = str2Float(stockDetails.get(39));
            totalIncrease = Float.compare(totalIncreaseTemp, Float.MAX_VALUE) != 0 ? totalIncreaseTemp : 0F;
        }
        stockDto.setTotalIncrease(totalIncrease);


        float signProfit = 0F;
        if (Float.compare(fFxj, Float.MAX_VALUE) != 0 && Float.compare(fFxj, 0F) != 0 && Float.compare(totalIncrease, 0F) != 0) {
            signProfit = StringUtils.equals(mkt, "sz") ? Math.round(500 * fFxj * totalIncrease / 100) : Math.round(1000 * fFxj * totalIncrease / 100);
        }
        stockDto.setSignProfit(signProfit);

        try {
            if (StringUtils.isNotBlank(stockDetails.get(13))) {
                DateTime dateTime = dateFormatToDay.parseDateTime(stockDetails.get(13));
                if (null != dateTime) {
                    stockDto.setMarketDate(new Date(dateTime.getMillis()));
                }
            }
        } catch (Exception e) {
            logger.error("parse data failed! the value is: " + stockDetails.get(13), e);
        }
        stockDto.setCrawlDate(new Date(System.currentTimeMillis()));

        return stockDto;
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

    /**
     * 将字符串转化为float
     *
     * @param str 带转化字符串
     * @return 字符串的float格式
     */
    private Float str2Float(String str) {
        if (StringUtils.isBlank(str)) {
            return Float.MAX_VALUE;
        }

        Float value = Float.valueOf(0);
        try {
            value = Float.parseFloat(str);
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
            value = Float.MAX_VALUE;
        }

        return value;
    }

}
