package com.auto.trade.services;

import com.auto.model.ApiBinance;
import com.auto.model.entity.TradeSymbol;
import com.auto.trade.entity.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by caigaonian870 on 17/12/11.
 */
@Service
public class DataService<T extends Kline> {

    @Autowired
    private Kline1minuteRepository kline1minuteRepository;

    @Autowired
    private DepthDataRepository depthDataRepository;
    @Autowired
    private OrderPriceRepository orderPriceRepository;

    private DateTime lastCashDateTime = new DateTime().withYear(2010);

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public OrderPrice saveOrderPrice(OrderPrice orderPrice){
        orderPrice = orderPriceRepository.save(orderPrice);
        return orderPrice;
    }

    public OrderPrice queryOrderPrice(long eventTime){
        Query query = this.entityManager.createQuery("from OrderPrice k where k.eventTime=:eventTime",OrderPrice.class);
        query.setParameter("eventTime",eventTime);
        try{
            OrderPrice orderPrice = (OrderPrice)query.getSingleResult();
            return orderPrice;

        }catch (NoResultException e){
            return null;
        }
    }

    @Transactional
    public DepthData saveDepthData(DepthData depthData){
        depthData = depthDataRepository.save(depthData);
        return depthData;
    }

    public DepthData queryLatestDepthData(Exchange exchange, TradeSymbol symbol){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyyMMddHHmmss");

        DateTime to = new DateTime();
        DateTime from = to.minusMinutes(1);

        Query query = this.entityManager.createNativeQuery("select * "+
                    "from depth_data " +
                "where symbol=:symbol and exchange=:exchange and date_created > :fromDate"+
                " and date_created <= :toDate order by event_time desc limit 10",DepthData.class);
        try{
            query.setParameter("symbol", ApiBinance.getSymbol(symbol));
            query.setParameter("exchange",exchange.getExchange());
            query.setParameter("fromDate",Long.valueOf(dateTimeFormatter.print(from)));
            query.setParameter("toDate",Long.valueOf(dateTimeFormatter.print(to)));
            List<DepthData> depthDataList = (List<DepthData>)query.getResultList();
            if(depthDataList.size()>0){
                return depthDataList.get(0);
            }
            return null;

        }catch (NoResultException e){
            return null;
        }
    }

    public DepthData queryDepthData(long eventTime,long updateId){
        Query query = this.entityManager.createQuery("from DepthData k where k.eventTime=:eventTime and k.updateId=:updateId",DepthData.class);
        query.setParameter("eventTime",eventTime);
        query.setParameter("updateId",updateId);
        try{
            DepthData depthData = (DepthData)query.getSingleResult();
            return depthData;

        }catch (NoResultException e){
            return null;
        }
    }

    public Kline1minute queryKline1minuteById(Integer id){
        Query query = this.entityManager.createQuery("from Kline1minute k where k.id=:id",Kline1minute.class);
        query.setParameter("id",id);
        try{
            Kline1minute kline1minute = (Kline1minute)query.getSingleResult();
            return kline1minute;

        }catch (NoResultException e){
            return null;
        }
    }


    @Transactional
    public Kline1minute saveKline1minute(Kline1minute kline1minute){
        kline1minute = kline1minuteRepository.save(kline1minute);
        return kline1minute;
    }

    public List<Kline1minute> queryKline1minuteListWithTimeWidnow(DateTime from, DateTime to){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyyMMddHHmmss");


        Query query = this.entityManager.createNativeQuery("select * "+
                "from kline_1minute " +
                "where date_created > :fromDate"+
                " and date_created <= :toDate",Kline1minute.class);
        try{
            query.setParameter("fromDate",Long.valueOf(dateTimeFormatter.print(from)));
            query.setParameter("toDate",Long.valueOf(dateTimeFormatter.print(to)));
            List<Kline1minute> kline1minuteList = (List<Kline1minute>)query.getResultList();
            return kline1minuteList;

        }catch (NoResultException e){
            return null;
        }
    }

}
