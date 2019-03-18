package org.crm.model.repository.impl;

import org.crm.common.QueryUtils;
import org.crm.model.dto.DirectSalesDTO;
import org.crm.model.repository.AnalysisRepository;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AnalysisRepositoryImpl implements AnalysisRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Map<String, Object>> findStatisBySaleChannel(DirectSalesDTO condition) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql= new StringBuilder();
        sql.append("select sales_channel as channel, count(*) as count from direct_sales s where 1=1 ");
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
        sql.append(" group by sales_channel ");
        Query query = this.entityManager.createNativeQuery(sql.toString());
        QueryUtils.setParams(query, params);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    @Override
    public List<Map<String, Object>> findCustomerSalesRank(DirectSalesDTO condition, int maxResults) {
        StringBuilder sql= new StringBuilder();
        sql.append("select customer_id as customerId, customer_name as customerName,sum(sales_count) as total from all_channel_sales s where 1=1 ");

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
    public List<Map<String, Object>> findSalesCountPerMonth(DirectSalesDTO condition) {
        StringBuilder sql= new StringBuilder();
        sql.append("select date_format(s.sales_date, '%Y%m') as date,");
        sql.append("sum(case when s.sales_channel = '直销' then s.sales_count else 0 end) channel_zx,");
        sql.append("sum(case when s.sales_channel = '批发' then s.sales_count else 0 end) channel_pf ");
        sql.append("from direct_sales s where 1=1 ");

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
        sql.append("GROUP BY date_format(s.sales_date, '%Y%m')");
        Query query = this.entityManager.createNativeQuery(sql.toString());
        QueryUtils.setParams(query, params);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

}
