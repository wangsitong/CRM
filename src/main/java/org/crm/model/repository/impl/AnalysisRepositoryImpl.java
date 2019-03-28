package org.crm.model.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.crm.common.QueryUtils;
import org.crm.model.dto.SalesDTO;
import org.crm.model.repository.AnalysisRepository;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class AnalysisRepositoryImpl implements AnalysisRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Map<String, Object>> findStatisBySaleChannel(SalesDTO condition) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql= new StringBuilder();
        sql.append("select sales_channel as channel, sum(s.sales_count) as count from sales s where 1=1 ");
        if (condition != null) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            if (condition.getStartSalesDate() != null) {
                sql.append("and s.sales_date >= :startSalesDate ");
                params.put("startSalesDate", df.format(condition.getStartSalesDate()));
            }
            if (condition.getEndSalesDate() != null) {
                sql.append("and s.sales_date <= :endSalesDate ");
                params.put("endSalesDate", df.format(condition.getEndSalesDate()));
            }
            if (StringUtils.isNotBlank(condition.getSalesStation())) {
                sql.append("and s.sales_station = :station ");
                params.put("station", condition.getSalesStation());
            }
        }
        sql.append(" group by sales_channel ");
        Query query = this.entityManager.createNativeQuery(sql.toString());
        QueryUtils.setParams(query, params);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    @Override
    public double findSalesCount(SalesDTO condition) {
        StringBuilder sql= new StringBuilder();
        sql.append("select sum(s.sales_count) as count from sales s where 1=1 ");
        Map<String, Object> params = new HashMap<>();
        if (condition != null) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            if (condition.getStartSalesDate() != null) {
                sql.append("and s.sales_date >= :startSalesDate ");
                params.put("startSalesDate", df.format(condition.getStartSalesDate()));
            }
            if (condition.getEndSalesDate() != null) {
                sql.append("and s.sales_date <= :endSalesDate ");
                params.put("endSalesDate", df.format(condition.getEndSalesDate()));
            }
            if (StringUtils.isNotBlank(condition.getSalesStation())) {
                sql.append("and s.sales_station = :station ");
                params.put("station", condition.getSalesStation());
            }
            if (StringUtils.isNotBlank(condition.getSalesStationNotEquals())) {
                sql.append("and s.sales_station <> :salesStationNotEquals ");
                params.put("salesStationNotEquals", condition.getSalesStationNotEquals());
            }
        }
        Query query = this.entityManager.createNativeQuery(sql.toString());
        QueryUtils.setParams(query, params);
        return ((Number)query.getSingleResult()).doubleValue();
    }

    @Override
    public List<Map<String, Object>> findCustomerSalesRank(SalesDTO condition, int maxResults) {
        StringBuilder sql= new StringBuilder();
        sql.append("select customer_id as customerId, customer_name as customerName,sum(sales_count) as total from sales s where 1=1 ");

        Map<String, Object> params = new HashMap<>();
        params.put("maxResults", maxResults);
        if (condition != null) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            if (condition.getStartSalesDate() != null) {
                sql.append("and s.sales_date >= :startSalesDate ");
                params.put("startSalesDate", df.format(condition.getStartSalesDate()));
            }
            if (condition.getEndSalesDate() != null) {
                sql.append("and s.sales_date <= :endSalesDate ");
                params.put("endSalesDate", df.format(condition.getEndSalesDate()));
            }
        }
        sql.append("group by customer_id, customer_name order by total desc limit :maxResults offset 0");
        Query query = this.entityManager.createNativeQuery(sql.toString());
        QueryUtils.setParams(query, params);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    @Override
    public List<Map<String, Object>> findSalesCountPerMonth(SalesDTO condition) {
        StringBuilder sql= new StringBuilder();
        sql.append("select date_format(s.sales_date, '%Y%m') as date,");
        sql.append("sum(case when s.sales_channel = '直销' then s.sales_count else 0 end) channel_zx,");
        sql.append("sum(case when s.sales_channel = '分销' then s.sales_count else 0 end) channel_fx ");
        sql.append("from sales s where 1=1 ");

        Map<String, Object> params = new HashMap<>();
        if (condition != null) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            if (condition.getStartSalesDate() != null) {
                sql.append("and s.sales_date >= :startSalesDate ");
                params.put("startSalesDate", df.format(condition.getStartSalesDate()));
            }
            if (condition.getEndSalesDate() != null) {
                sql.append("and s.sales_date <= :endSalesDate ");
                params.put("endSalesDate", df.format(condition.getEndSalesDate()));
            }
        }
        sql.append("GROUP BY date_format(s.sales_date, '%Y%m') ");
        sql.append("order by date_format(s.sales_date, '%Y%m')");
        Query query = this.entityManager.createNativeQuery(sql.toString());
        QueryUtils.setParams(query, params);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    @Override
    public List<Map<String, Object>> findManagerSales(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-01");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        String startTime = df.format(c.getTime());
        c.add(Calendar.MONTH, 1);
        String endTime = df.format(c.getTime());

        StringBuilder sql = new StringBuilder();
        sql.append("select m.manager_id, m.manager_name,c.customer_area, sum(sales_count) as count, ");
        sql.append("sum(case when s.sales_oil like '%汽油%' then s.sales_count else 0 end) gas, ");
        sql.append("sum(case when s.sales_oil like '%柴油%' then s.sales_count else 0 end) diesel ");
        sql.append("from manager m ");
        sql.append("left join sales s on m.manager_id = s.manager_id ");
        sql.append("left join customer c on c.customer_id = s.customer_id ");
        sql.append("where 1=1 ");
        sql.append("and c.customer_id in (select customer_id from private_station) ");
        sql.append("and sales_date >= :startTime ");
        sql.append("and sales_date < :endTime ");
        sql.append("group by m.manager_id, m.manager_name,c.customer_area ");
        sql.append("order by c.customer_area ");

        Query query = this.entityManager.createNativeQuery(sql.toString());
        query.setParameter("startTime", startTime);
        query.setParameter("endTime", endTime);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

}
