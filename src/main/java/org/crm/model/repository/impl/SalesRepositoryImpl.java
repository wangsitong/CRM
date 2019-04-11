package org.crm.model.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.crm.common.QueryUtils;
import org.crm.model.dto.SalesDTO;
import org.crm.model.entity.Sales;
import org.crm.model.repository.SalesRepository;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SalesRepositoryImpl implements SalesRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Sales findById(String id) {
        return this.entityManager.find(Sales.class, id);
    }

    @Override
    public List<?> findByCondition(SalesDTO condition, int fistResult, int maxResults) {
        StringBuilder hql = new StringBuilder();
        hql.append("select s.id,s.sales_channel salesChannel,s.sales_date salesDate,s.customer_id customerId,");
        hql.append("s.customer_name customerName,s.sales_oil salesOil,s.manager_id managerId,");
        hql.append("s.manager_name managerName,s.sales_station salesStation,");
        hql.append("s.sales_count salesCount,s.sales_price salesPrice,s.is_transfer transfer,");
        hql.append("s.original_manager_id originalManagerId,s.original_manager_name originalManagerName,");
        hql.append("c.customer_area as customerArea from sales s ");
        hql.append("left join customer c on s.customer_id = c.customer_id where 1=1 ");
        Map<String, Object> params = this.setQueryParams(condition, hql);
        hql.append("order by s.sales_date desc");
        Query query = this.entityManager.createNativeQuery(hql.toString());
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(SalesDTO.class));
        QueryUtils.setParams(query, params);
        query.setFirstResult(fistResult);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    @Override
    public int findCount(SalesDTO condition) {
        StringBuilder hql = new StringBuilder();
        hql.append("select count(s.id) from sales s ");
        hql.append("left join customer c on s.customer_id = c.customer_id where 1=1 ");
        Map<String, Object> params = this.setQueryParams(condition, hql);
        Query query = this.entityManager.createNativeQuery(hql.toString());
        QueryUtils.setParams(query, params);
        return ((Number)query.getSingleResult()).intValue();
    }

    private Map<String, Object> setQueryParams(SalesDTO condition, StringBuilder hql) {
        Map<String, Object> params = new HashMap<>();
        if (condition != null) {
            if (StringUtils.isNotBlank(condition.getCustomerId())) {
                hql.append("and s.customer_d = :customerId ");
                params.put("customerId", condition.getCustomerId());
            }
            if (StringUtils.isNotBlank(condition.getSalesStation())) {
                hql.append("and s.sales_station like :station ");
                params.put("station", "%" + condition.getSalesStation() + "%");
            }
            if (StringUtils.isNotBlank(condition.getSalesChannel())) {
                hql.append("and s.sales_channel = :salesChannel ");
                params.put("salesChannel", condition.getSalesChannel());
            }
            if (condition.getStartSalesDate() != null) {
                hql.append("and s.sales_date >= :startDate ");
                params.put("startDate", condition.getStartSalesDate());
            }
            if (condition.getEndSalesDate() != null) {
                hql.append("and s.sales_date <= :endDate ");
                params.put("endDate", condition.getEndSalesDate());
            }
            if (StringUtils.isNotBlank(condition.getSalesStationNotEquals())) {
                hql.append("and s.sales_station <> :stationNotEquals ");
                params.put("stationNotEquals", condition.getSalesStationNotEquals());
            }
            if (StringUtils.isNotBlank(condition.getTransfer())) {
                hql.append("and s.is_transfer = :transfer ");
                params.put("transfer", condition.getTransfer());
            }
            if (StringUtils.isNotBlank(condition.getCustomerArea())) {
                hql.append("and c.customer_area = :area ");
                params.put("area", condition.getCustomerArea());
            }
        }
        return params;
    }

    public List<Sales> findExact(Sales condition) {
        StringBuilder hql = new StringBuilder("select s from Sales s where 1=1 ");
        Map<String, Object> params = new HashMap<>();
        if (condition != null) {
            if (StringUtils.isNotBlank(condition.getCustomerId())) {
                hql.append("and customerId = :customerId ");
                params.put("customerId", condition.getCustomerId());
            }
            if (StringUtils.isNotBlank(condition.getSalesChannel())) {
                hql.append("and salesChannel = :salesChannel ");
                params.put("salesChannel", condition.getSalesChannel());
            }
            if (condition.getSalesDate() != null) {
                hql.append("and salesDate = :salesDate ");
                params.put("salesDate", condition.getSalesDate());
            }
            if (condition.getManagerId() != null) {
                hql.append("and managerId = :managerId ");
                params.put("managerId", condition.getManagerId());
            }
            if (condition.getSalesOil() != null) {
                hql.append("and salesOil = :salesOil ");
                params.put("salesOil", condition.getSalesOil());
            }
            if (condition.getSalesCount() != null) {
                hql.append("and salesCount = :salesCount ");
                params.put("salesCount", condition.getSalesCount());
            }
            if (condition.getSalesPrice() != null) {
                hql.append("and salesPrice = :salesPrice ");
                params.put("salesPrice", condition.getSalesPrice());
            }
        }

        Query query = this.entityManager.createQuery(hql.toString());
        QueryUtils.setParams(query, params);
        return query.getResultList();
    }

    public List<Sales> findByNeedTransfer(SalesDTO condition) {
        StringBuilder sql = new StringBuilder();
        sql.append("select s from Sales s ");
        sql.append("where s.salesChannel = '零售' and s.salesStation <> '#' ");
        sql.append("and s.customerId in (");
        sql.append("select customerId from Sales where salesChannel <> '零售') ");

        Map<String, Object> params = this.setQueryParams(condition, sql);

        Query query = this.entityManager.createQuery(sql.toString());
        QueryUtils.setParams(query, params);
        return query.getResultList();
    }

    @Override
    public void save(Sales sales) {
        this.entityManager.persist(sales);
    }

    @Override
    public void delete(String id) {
        Sales instance = this.entityManager.find(Sales.class, id);
        this.entityManager.remove(instance);
    }
}
