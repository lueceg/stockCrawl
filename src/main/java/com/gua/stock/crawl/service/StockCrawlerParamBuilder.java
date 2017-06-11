package com.gua.stock.crawl.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

import com.gua.open.connection.parameter.RequestParams;

/**
 * 类StockCrawlerParamBuilder.java的实现描述：TODO 类实现描述
 * 
 * @author weicheng.lwc 2017年6月11日 下午4:10:58
 */
@Service
public class StockCrawlerParamBuilder {

    private final static String extraParams = "type=NS&sty=NSSTV5&st=12&sr=-1&ps=50&stat=1&rt=49727588";

    public RequestParams build(Integer pageNum) {
        RequestParams params = new RequestParams();
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if (null != pageNum && pageNum >= 0) {
            nameValuePairs.add(new BasicNameValuePair("p", pageNum.toString()));
        }

        params.setNameValuePairs(nameValuePairs);
        params.setExtraParams(extraParams);

        return params;
    }

}
