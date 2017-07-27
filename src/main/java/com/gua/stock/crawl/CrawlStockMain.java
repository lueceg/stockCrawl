package com.gua.stock.crawl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

import com.gua.open.mybatis.dto.StockDao;
import com.gua.open.service.StockService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.gua.stock.crawl.service.CrawlStockService;

/**
 * 类CrawlStockMain.java的实现描述：TODO 类实现描述 
 * @author weicheng.lwc 2017年6月12日 下午7:26:29
 */
public class CrawlStockMain {
    
    private static ApplicationContext context = new ClassPathXmlApplicationContext("service-beans.xml");

    private final static Integer MAX_PAGE_INDEX = 50;

    private final static int MAX_POOL_SIZE = 10;

    private static StockService stockService;
    
    public static void main(String[] args) {
        CrawlStockService crawlStockService = context.getBean(CrawlStockService.class);
        stockService = context.getBean(StockService.class);
        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(MAX_POOL_SIZE);
        List<Future<List<StockDao>>> futureList = new ArrayList<>();
        for (int pageIndex = 1; pageIndex <= MAX_PAGE_INDEX; pageIndex++) {
            Future<List<StockDao>> future = newFixedThreadPool.submit(new CrawlStockTask(crawlStockService, pageIndex));
            futureList.add(future);
        }
        
        List<StockDao> totalStockDtoList = new ArrayList<>();
        for (Future<List<StockDao>> future : futureList) {
            List<StockDao> list = null;
            try {
                list = future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            totalStockDtoList.addAll(list);
        }
        newFixedThreadPool.shutdown();

        System.out.println("获取数量：" + totalStockDtoList.size());
//        int index = 1;
//        for (StockDto dto : totalStockDtoList) {
//            System.out.println(String.valueOf(index++) + ": " + dto);
//
//        }

        // 用stream的方式过滤出id不重复的DTO

        Set<Integer> stockCodeSet = totalStockDtoList.stream().map(stockDto -> stockDto.getStockCode()).collect(Collectors.toSet());
        List<StockDao> totalStockDtos = totalStockDtoList.stream().filter(stockDto -> {
            if (stockCodeSet.contains(stockDto.getStockCode())) {
                stockCodeSet.remove(stockDto.getStockCode());
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
        System.out.println("实际存储数量：" + totalStockDtos.size());
        stockService.deleteAll();
        stockService.insertBatch(totalStockDtos);
    }
    
}
