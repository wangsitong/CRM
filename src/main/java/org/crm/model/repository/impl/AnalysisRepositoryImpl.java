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
        sql.append("and s.sales_channel in ('直销', '分销') ");
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
            if (StringUtils.isNotBlank(condition.getTransfer())) {
                sql.append("and s.is_transfer = :transfer ");
                params.put("transfer", condition.getTransfer());
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
        Object result = query.getSingleResult();
        if (result != null) {
            return ((Number) result).doubleValue();
        }
        return 0;
    }

    @Override
    public List<Map<String, Object>> findCustomerSalesRank(SalesDTO condition, int maxResults) {
        StringBuilder sql= new StringBuilder();
        sql.append("select s.customer_id as customerId, s.customer_name as customerName,sum(sales_count) as total from sales s ");
        sql.append("left join customer c on s.customer_id = c.customer_id where 1=1 ");
        sql.append("and ifnull(c.analysis_exclude, '') <> '1' ");

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
        sql.append("group by s.customer_id, s.customer_name order by total desc limit :maxResults offset 0");
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
            if (StringUtils.isNotBlank(condition.getSalesChannelNotEquals())) {
                sql.append("and s.sales_channel <> :salesChannelNotEquals ");
                params.put("salesChannelNotEquals", condition.getSalesChannelNotEquals());
            }
            if ("1".equals(condition.getTransfer())) {
                sql.append("and s.is_transfer = :transfer ");
                params.put("transfer", condition.getTransfer());
            } else if ("0".equals(condition.getTransfer())) {
                sql.append("and ifnull(s.is_transfer, '') <> '1' ");
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
    public List<Map<String, Object>> findManagerSales(SalesDTO condition) {
        StringBuilder sql = new StringBuilder();
        sql.append("select s.manager_id as managerId,s.manager_name as managerName,");
        sql.append("sum(case when s.sales_oil like '%汽油%' then s.sales_count else 0 end) totalOfGas,");
        sql.append("sum(case when s.sales_oil like '%柴油%' then s.sales_count else 0 end) totalOfDiesel ");
        sql.append("from (");
        sql.append("select s.* from sales s ");
        sql.append("where sales_station = '#' ");
        sql.append("and s.customer_id in (select customer_id from private_station) ");
        sql.append("union all ");
        sql.append("select s.* from sales s where s.is_transfer = '1') as s where 1=1 ");

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
        sql.append("group by s.manager_id,s.manager_name");
        Query query = this.entityManager.createNativeQuery(sql.toString());
        QueryUtils.setParams(query, params);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

}
