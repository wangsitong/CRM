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
        sql.append("select sales_channel as channel, sum(s.sales_count) as count from sales s where s.customer_id <> '40257480' ");
        sql.append("and s.sales_channel <> '零售' ");
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
        sql.append("select sum(s.sales_count) as count from sales s where s.customer_id <> '40257480' ");
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
        sql.append("select s.customer_id as customerId, s.customer_name as customerName,");
        sql.append("c.customer_manager as customerManager,sum(sales_count) as total from sales s ");
        sql.append("left join customer c on s.customer_id = c.customer_id where s.customer_id <> '40257480' ");
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
        sql.append("group by s.customer_id, s.customer_name, c.customer_manager order by total desc limit :maxResults offset 0");
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
        sql.append("from sales s where s.customer_id <> '40257480' ");

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
        sql.append("select m.manager_id as managerId,m.manager_name as managerName,a.totalOfGas,a.totalOfDiesel ");
        sql.append("from manager m left join (");
        sql.append("select s.manager_name as managerName,");
        sql.append("sum(case when s.sales_oil like '%汽油%' then s.sales_count else 0 end) totalOfGas,");
        sql.append("sum(case when s.sales_oil like '%柴油%' then s.sales_count else 0 end) totalOfDiesel ");
        sql.append("from ( ");
        sql.append("select s.sales_date,s.manager_id,s.manager_name,s.sales_oil,s.sales_count ");
        sql.append("from sales s ");
        sql.append("where s.customer_id <> '40257480' and s.customer_id not in (select customer_id from customer where customer_manager = '大客户') ");
        sql.append("AND sales_channel <> '零售' ");
        sql.append("union all ");
        sql.append("select s.sales_date,s.original_manager_id,s.original_manager_name as manager_name,s.sales_oil,s.sales_count ");
        sql.append("from sales s where s.is_transfer = '1' ");
        sql.append(") s where 1=1 ");

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

        sql.append("group by s.manager_name ");
        sql.append(") a on m.manager_name = a.managerName");

        Query query = this.entityManager.createNativeQuery(sql.toString());
        QueryUtils.setParams(query, params);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    public List<Map<String, Object>> findByStationAndArea(SalesDTO condition, int firstResult, int maxResults) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT s.customer_id customerId, s.customer_name customerName,p.customer_area area,");
        sql.append("SUM(CASE WHEN s.sales_oil LIKE '%汽油%' THEN s.sales_count ELSE 0 END) gas,");
        sql.append("SUM(CASE WHEN s.sales_oil LIKE '%柴油%' THEN s.sales_count ELSE 0 END) diesel,");
        sql.append("SUM(s.sales_count) total ");
        sql.append("FROM sales s ");
        sql.append("LEFT JOIN private_station p ON s.customer_id = p.customer_id ");
        sql.append("WHERE s.customer_id <> '40257480' and s.customer_id IN (SELECT customer_id FROM private_station) ");

        Map<String, Object> params = this.setQueryParamsByStationsAndArea(sql, condition);
        sql.append("GROUP BY  s.customer_id,s.customer_name,p.customer_area ");
        sql.append("order by p.customer_area,total desc ");
        Query query = this.entityManager.createNativeQuery(sql.toString());
        QueryUtils.setParams(query, params);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    public int findCountByStationsAndArea(SalesDTO condition) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(a.customerId) from (");
        sql.append("SELECT s.customer_id customerId, s.customer_name customerName,p.customer_area customerArea,");
        sql.append("SUM(CASE WHEN s.sales_oil LIKE '%汽油%' THEN s.sales_count ELSE 0 END) gas,");
        sql.append("SUM(CASE WHEN s.sales_oil LIKE '%柴油%' THEN s.sales_count ELSE 0 END) diesel,");
        sql.append("SUM(s.sales_count) total ");
        sql.append("FROM sales s ");
        sql.append("LEFT JOIN private_station p ON s.customer_id = p.customer_id ");
        sql.append("WHERE s.customer_id <> '40257480' and s.customer_id IN (SELECT customer_id FROM private_station) ");

        Map<String, Object> params = this.setQueryParamsByStationsAndArea(sql, condition);
        sql.append("GROUP BY  s.customer_id,s.customer_name,p.customer_area ");
        sql.append("order by p.customer_area) as a");
        Query query = this.entityManager.createNativeQuery(sql.toString());
        QueryUtils.setParams(query, params);
        return ((Number)query.getSingleResult()).intValue();
    }

    private Map<String, Object> setQueryParamsByStationsAndArea(StringBuilder sql, SalesDTO condition) {
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
            if (StringUtils.isNotBlank(condition.getCustomerArea())) {
                sql.append("and p.customer_area = :area ");
                params.put("area", condition.getCustomerArea());
            }
        }
        return params;
    }

    public List<Map<String, Object>> findByManagerAndOilsCategory(SalesDTO condition) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT s.manager_name managerName,");
        sql.append("SUM(CASE WHEN s.sales_oil LIKE '%汽油%' THEN s.sales_count ELSE 0 END) gas,");
        sql.append("SUM(CASE WHEN s.sales_oil LIKE '%柴油%' THEN s.sales_count ELSE 0 END) diesel,");
        sql.append("SUM(s.sales_count) total ");
        sql.append("FROM (");

        sql.append("select s.sales_date,s.manager_id,s.manager_name,s.sales_oil,s.sales_count ");
        sql.append("from sales s ");
        sql.append("where s.customer_id <> '40257480' and s.customer_id not in (select customer_id from customer where customer_manager = '大客户') ");
        sql.append("AND sales_channel <> '零售' ");
        sql.append("union all ");
        sql.append("select s.sales_date,s.original_manager_id as manager_id,s.original_manager_name as manager_name,s.sales_oil,s.sales_count ");
        sql.append("from sales s where s.is_transfer = '1' and s.original_manager_name is not null ");

        sql.append(") s WHERE 1=1 ");

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
        sql.append("GROUP BY s.manager_name");

        Query query = this.entityManager.createNativeQuery(sql.toString());
        QueryUtils.setParams(query, params);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    public Map<String, Object> findBySalesCountRange(SalesDTO condition) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SUM(CASE WHEN  t.total  >= 2000 THEN 1 ELSE 0 END) t0,");
        sql.append("SUM(CASE WHEN t.total < 2000 AND t.total >= 1500 THEN 1 ELSE 0 END) t1,");
        sql.append("SUM(CASE WHEN t.total < 1500 AND t.total >= 1200 THEN 1 ELSE 0 END) t2,");
        sql.append("SUM(CASE WHEN t.total < 1200 AND t.total >= 1000 THEN 1 ELSE 0 END) t3,");
        sql.append("SUM(CASE WHEN t.total < 1000 AND t.total >= 500 THEN 1 ELSE 0 END) t4,");
        sql.append("SUM(CASE WHEN t.total < 500 AND t.total >= 200 THEN 1 ELSE 0 END) t5,");
        sql.append("SUM(CASE WHEN t.total < 200 AND t.total >= 0 THEN 1 ELSE 0 END) t6 ");
        sql.append("FROM (SELECT s.customer_id ,s.customer_name,");
        sql.append("SUM(s.sales_count) AS total ");
        sql.append("FROM sales s WHERE s.customer_id <> '40257480' and sales_channel <> '零售' ");

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

        sql.append("GROUP BY s.customer_id ,s.customer_name ");
        sql.append("ORDER BY  total DESC) t");

        Query query = this.entityManager.createNativeQuery(sql.toString());
        QueryUtils.setParams(query, params);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<?> dataList = query.getResultList();
        if (dataList != null && !dataList.isEmpty()) {
            return (Map<String, Object>) dataList.get(0);
        }
        return null;
    }

    public List<Map<String, Object>> findByCustomerSalesCount(SalesDTO condition) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT s.customer_id as customerId,s.customer_name customerName,");
        sql.append("SUM(s.sales_count) AS total ");
        sql.append("FROM sales s WHERE s.customer_id <> '40257480' and s.sales_channel  <> '零售' ");

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
        sql.append("GROUP BY s.customer_id,s.customer_name ");
        sql.append("ORDER BY total DESC");

        Query query = this.entityManager.createNativeQuery(sql.toString());
        QueryUtils.setParams(query, params);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    public List<Map<String, Object>> findByManagerAndOilsCategoryPerMonth(int year) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT s.manager_name managerName,");
        sql.append("SUM(CASE WHEN DATE_FORMAT(s.sales_date,'%m') = '01' AND s.sales_oil LIKE '%汽油%' THEN s.sales_count ELSE 0 END) t1,");
        sql.append("SUM(CASE WHEN DATE_FORMAT(s.sales_date,'%m') = '01' AND s.sales_oil LIKE '%柴油%' THEN s.sales_count ELSE 0 END) t1_,");
        sql.append("SUM(CASE WHEN DATE_FORMAT(s.sales_date,'%m') = '02' AND s.sales_oil LIKE '%汽油%' THEN s.sales_count ELSE 0 END) t2,");
        sql.append("SUM(CASE WHEN DATE_FORMAT(s.sales_date,'%m') = '02' AND s.sales_oil LIKE '%柴油%' THEN s.sales_count ELSE 0 END) t2_,");
        sql.append("SUM(CASE WHEN DATE_FORMAT(s.sales_date,'%m') = '03' AND s.sales_oil LIKE '%汽油%' THEN s.sales_count ELSE 0 END) t3,");
        sql.append("SUM(CASE WHEN DATE_FORMAT(s.sales_date,'%m') = '03' AND s.sales_oil LIKE '%柴油%' THEN s.sales_count ELSE 0 END) t3_,");
        sql.append("SUM(CASE WHEN DATE_FORMAT(s.sales_date,'%m') = '04' AND s.sales_oil LIKE '%汽油%' THEN s.sales_count ELSE 0 END) t4,");
        sql.append("SUM(CASE WHEN DATE_FORMAT(s.sales_date,'%m') = '04' AND s.sales_oil LIKE '%柴油%' THEN s.sales_count ELSE 0 END) t4_,");
        sql.append("SUM(CASE WHEN DATE_FORMAT(s.sales_date,'%m') = '05' AND s.sales_oil LIKE '%汽油%' THEN s.sales_count ELSE 0 END) t5,");
        sql.append("SUM(CASE WHEN DATE_FORMAT(s.sales_date,'%m') = '05' AND s.sales_oil LIKE '%柴油%' THEN s.sales_count ELSE 0 END) t5_,");
        sql.append("SUM(CASE WHEN DATE_FORMAT(s.sales_date,'%m') = '06' AND s.sales_oil LIKE '%汽油%' THEN s.sales_count ELSE 0 END) t6,");
        sql.append("SUM(CASE WHEN DATE_FORMAT(s.sales_date,'%m') = '06' AND s.sales_oil LIKE '%柴油%' THEN s.sales_count ELSE 0 END) t6_,");
        sql.append("SUM(CASE WHEN DATE_FORMAT(s.sales_date,'%m') = '07' AND s.sales_oil LIKE '%汽油%' THEN s.sales_count ELSE 0 END) t7,");
        sql.append("SUM(CASE WHEN DATE_FORMAT(s.sales_date,'%m') = '07' AND s.sales_oil LIKE '%柴油%' THEN s.sales_count ELSE 0 END) t7_,");
        sql.append("SUM(CASE WHEN DATE_FORMAT(s.sales_date,'%m') = '08' AND s.sales_oil LIKE '%汽油%' THEN s.sales_count ELSE 0 END) t8,");
        sql.append("SUM(CASE WHEN DATE_FORMAT(s.sales_date,'%m') = '08' AND s.sales_oil LIKE '%柴油%' THEN s.sales_count ELSE 0 END) t8_,");
        sql.append("SUM(CASE WHEN DATE_FORMAT(s.sales_date,'%m') = '09' AND s.sales_oil LIKE '%汽油%' THEN s.sales_count ELSE 0 END) t9,");
        sql.append("SUM(CASE WHEN DATE_FORMAT(s.sales_date,'%m') = '09' AND s.sales_oil LIKE '%柴油%' THEN s.sales_count ELSE 0 END) t9_,");
        sql.append("SUM(CASE WHEN DATE_FORMAT(s.sales_date,'%m') = '10' AND s.sales_oil LIKE '%汽油%' THEN s.sales_count ELSE 0 END) t10,");
        sql.append("SUM(CASE WHEN DATE_FORMAT(s.sales_date,'%m') = '10' AND s.sales_oil LIKE '%柴油%' THEN s.sales_count ELSE 0 END) t10_,");
        sql.append("SUM(CASE WHEN DATE_FORMAT(s.sales_date,'%m') = '11' AND s.sales_oil LIKE '%汽油%' THEN s.sales_count ELSE 0 END) t11,");
        sql.append("SUM(CASE WHEN DATE_FORMAT(s.sales_date,'%m') = '11' AND s.sales_oil LIKE '%柴油%' THEN s.sales_count ELSE 0 END) t11_,");
        sql.append("SUM(CASE WHEN DATE_FORMAT(s.sales_date,'%m') = '12' AND s.sales_oil LIKE '%汽油%' THEN s.sales_count ELSE 0 END) t12,");
        sql.append("SUM(CASE WHEN DATE_FORMAT(s.sales_date,'%m') = '12' AND s.sales_oil LIKE '%柴油%' THEN s.sales_count ELSE 0 END) t12_ ");
        sql.append("FROM ( ");

        sql.append("select s.sales_date,s.manager_id,s.manager_name,s.sales_oil,s.sales_count ");
        sql.append("from sales s ");
        sql.append("where s.customer_id <> '40257480' and s.customer_id not in (select customer_id from customer where customer_manager = '大客户') ");
        sql.append("AND sales_channel <> '零售' ");
        sql.append("union all ");
        sql.append("select s.sales_date,s.original_manager_id as manager_id,s.original_manager_name as manager_name,s.sales_oil,s.sales_count ");
        sql.append("from sales s where s.is_transfer = '1' and s.original_manager_name is not null ");

        sql.append(") s WHERE s.sales_date>= :startDate AND s.sales_date <= :endDate ");
        sql.append("GROUP BY s.manager_name");

        Query query = this.entityManager.createNativeQuery(sql.toString());
        query.setParameter("startDate", year + "-01-01");
        query.setParameter("endDate", year + "-12-31");
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    @Override
    public List<Map<String, Object>> findBySalesArea(SalesDTO condition) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.area, b.gas,b.diesel,b.total from (");
        sql.append("select distinct customer_area as area from private_station where customer_area is not null) a ");
        sql.append("left join (");
        sql.append("SELECT p.customer_area as area,");
        sql.append("SUM(CASE WHEN s.sales_oil LIKE '%汽油%' THEN s.sales_count ELSE 0 END) gas,");
        sql.append("SUM(CASE WHEN s.sales_oil LIKE '%柴油%' THEN s.sales_count ELSE 0 END) diesel,");
        sql.append("SUM(s.sales_count) total ");
        sql.append("FROM sales s ");
        sql.append("LEFT JOIN private_station p ON s.customer_id = p.customer_id ");
        sql.append("WHERE s.customer_id <> '40257480' and s.customer_id IN ");
        sql.append("(SELECT customer_id FROM private_station)");

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
            if (condition.isTransferIsNull() != null && condition.isTransferIsNull()) {
                sql.append("and s.is_transfer is null ");
            }
        }
        sql.append("GROUP BY p.customer_area) b on a.area = b.area");

        Query query = this.entityManager.createNativeQuery(sql.toString());
        QueryUtils.setParams(query, params);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    public List<Map<String, Object>> findBySelfSalesPerDays(SalesDTO condition) {
        StringBuilder sql = new StringBuilder();
        sql.append("select DATE_FORMAT(s.sales_date, '%d') as date,");
        sql.append("sum(case when s.sales_oil like '%汽油%' then s.sales_count else 0 end) gas,");
        sql.append("sum(case when s.sales_oil like '%柴油%' then s.sales_count else 0 end) diesel,");
        sql.append("sum(s.sales_count) total ");
        sql.append("from sales s ");
        sql.append("where s.customer_id <> '40257480' and (s.sales_channel <> '零售' or s.is_transfer = '1') ");

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
        sql.append("group by date order by date");

        Query query = this.entityManager.createNativeQuery(sql.toString());
        QueryUtils.setParams(query, params);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    @Override
    public List<Map<String, Object>> findByCustomerDemandExecuteRate(SalesDTO condition) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t.customer_id customerId,t.customer_name customerName,t.customer_area area,t.gas,t.diesel,t.total,");
        sql.append("TRUNCATE(c.customer_demand_gas/12,2) AS gas_plan,TRUNCATE(c.customer_demand_diesel/12,2) AS diesel_plan,");
        sql.append("CONCAT(TRUNCATE(t.gas/(c.customer_demand_gas/12)*100,2), '%') AS gas_demand_rate,");
        sql.append("CONCAT(TRUNCATE(t.diesel/(c.customer_demand_diesel/12)*100,2), '%') AS diesel_demand_rate ");
        sql.append("FROM (select * from customer_demand where customer_demand_year = :demandYear) c RIGHT JOIN ");
        sql.append("(SELECT s.customer_id,s.customer_name,p.customer_area,");
        sql.append("SUM(CASE WHEN s.sales_oil LIKE '%汽油%' THEN s.sales_count ELSE 0 END) gas,");
        sql.append("SUM(CASE WHEN s.sales_oil LIKE '%柴油%' THEN s.sales_count ELSE 0 END) diesel,");
        sql.append("SUM(s.sales_count) total ");
        sql.append("FROM sales s LEFT JOIN private_station p ON s.customer_id = p.customer_id ");
        sql.append("WHERE s.customer_id <> '40257480' and s.customer_id IN (SELECT customer_id FROM private_station) ");

        Calendar startSalesDate = Calendar.getInstance();
        startSalesDate.setTime(condition.getStartSalesDate());

        Map<String, Object> params = new HashMap<>();
        params.put("demandYear", startSalesDate.get(Calendar.YEAR));
        if (condition != null) {
            if (condition.getStartSalesDate() != null) {
                sql.append("AND DATE_FORMAT(s.sales_date,'%Y%m') = :date ");
                params.put("date", (new SimpleDateFormat("yyyyMM")).format(condition.getStartSalesDate()));
            }
            if (StringUtils.isNotBlank(condition.getCustomerArea())) {
                sql.append("and p.customer_area = :area ");
                params.put("area", condition.getCustomerArea());
            }
        }
        sql.append("GROUP BY s.customer_id, s.customer_name,p.customer_area) t ");
        sql.append("ON c.customer_id = t.customer_id where 1=1 ");

        if (condition != null) {
            if (condition.getCustomerId() != null) {
                sql.append("and t.customer_id = :customerId ");
                params.put("customerId", condition.getCustomerId());
            }
        }

        Query query = this.entityManager.createNativeQuery(sql.toString());
        QueryUtils.setParams(query, params);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    public List<Map<String, Object>> findCustomerByLastSalesDate(int firstResult, int maxResults) {
        StringBuilder sql = new StringBuilder();
        sql.append("select s.customer_id as customerId,s.customer_name as customerName, max(s.sales_date) lastDate ");
        sql.append("from sales s where s.customer_id <> '40257480' ");
        sql.append("group by s.customer_id,s.customer_name ");
        sql.append("having lastdate < date_add(now(), interval -3 MONTH) ");
        sql.append("order by lastDate desc");

        Query query = this.entityManager.createNativeQuery(sql.toString());
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    public int findCountByLastSalesDate() {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from (");
        sql.append("select s.customer_id as customerId,s.customer_name as customerName, max(s.sales_date) lastDate ");
        sql.append("from sales s where s.customer_id <> '40257480' ");
        sql.append("group by s.customer_id,s.customer_name ");
        sql.append("having lastdate < date_add(now(), interval -3 MONTH) ");
        sql.append(") a");

        Query query = this.entityManager.createNativeQuery(sql.toString());
        return ((Number)query.getSingleResult()).intValue();
    }

    public List<Map<String, Object>> findAreaDemand(int year) {
        StringBuilder sql = new StringBuilder();
        sql.append("select c.customer_area area,sum(c.customer_demand_gas) as gas,");
        sql.append("SUM(c.customer_demand_diesel) AS diesel,");
        sql.append("sum(c.customer_demand_gas+c.customer_demand_diesel) as total ");
        sql.append("from customer_demand c where c.customer_id in (select customer_id from private_station) ");
        sql.append("and c.customer_demand_year = :year ");
        sql.append("group by c.customer_area ");

        Query query = this.entityManager.createNativeQuery(sql.toString());
        query.setParameter("year", year);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    public Map<String, Object> findTotalByCatagory(SalesDTO condition) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append("sum(case when s.sales_oil like '%汽油%' then s.sales_count else 0 end) gas,");
        sql.append("sum(case when s.sales_oil like '%柴油%' then s.sales_count else 0 end) diesel,");
        sql.append("sum(s.sales_count) total ");
        sql.append("from sales s where 1=1 ");
        Map<String, Object> params = this.setNormalQueryParams(condition, sql);
        Query query = this.entityManager.createNativeQuery(sql.toString());
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        QueryUtils.setParams(query, params);
        return (Map<String, Object>) query.getResultList().get(0);
    }

    private Map<String, Object> setNormalQueryParams(SalesDTO condition, StringBuilder sql) {
        Map<String, Object> params = new HashMap<>();
        if (condition != null) {
            if (StringUtils.isNotBlank(condition.getCustomerId())) {
                sql.append("and s.customer_id = :customerId ");
                params.put("customerId", condition.getCustomerId());
            }
            if (StringUtils.isNotBlank(condition.getManagerName())) {
                sql.append("and s.manager_name = :managerName ");
                params.put("managerName", condition.getManagerName());
            }
            if (StringUtils.isNotBlank(condition.getSalesStation())) {
                sql.append("and s.sales_station = :salesStation ");
                params.put("salesStation", condition.getSalesStation());
            }
            if (StringUtils.isNotBlank(condition.getSalesChannel())) {
                sql.append("and s.sales_channel = :salesChannel ");
                params.put("salesChannel", condition.getSalesChannel());
            }
            if (condition.getStartSalesDate() != null) {
                sql.append("and s.sales_date >= :startDate ");
                params.put("startDate", condition.getStartSalesDate());
            }
            if (condition.getEndSalesDate() != null) {
                sql.append("and s.sales_date <= :endDate ");
                params.put("endDate", condition.getEndSalesDate());
            }
            if (StringUtils.isNotBlank(condition.getSalesStationNotEquals())) {
                sql.append("and s.sales_station <> :stationNotEquals ");
                params.put("stationNotEquals", condition.getSalesStationNotEquals());
            }
            if (StringUtils.isNotBlank(condition.getTransfer())) {
                sql.append("and s.is_transfer = :transfer ");
                params.put("transfer", condition.getTransfer());
            }
            if (condition.isTransferIsNull() != null && condition.isTransferIsNull()) {
                sql.append("and s.is_transfer is null ");
            }
            if (StringUtils.isNotBlank(condition.getCustomerArea())) {
                sql.append("and c.customer_area = :area ");
                params.put("area", condition.getCustomerArea());
            }
            if ("1".equals(condition.getExcludeCustomer())) {
                sql.append("and s.customer_id <> '40257480' ");
            }
        }
        return params;
    }

}
