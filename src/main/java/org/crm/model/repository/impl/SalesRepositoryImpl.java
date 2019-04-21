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
    public List<?> findByCondition(SalesDTO condition, int firstResult, int maxResults) {
        StringBuilder sql = new StringBuilder();
        sql.append("select s.id,s.sales_channel salesChannel,s.sales_date salesDate,s.customer_id customerId,");
        sql.append("s.customer_name customerName,s.sales_oil salesOil,s.manager_id managerId,");
        sql.append("s.manager_name managerName,s.sales_station salesStation,");
        sql.append("s.sales_count salesCount,s.sales_price salesPrice,s.is_transfer transfer,");
        sql.append("s.original_manager_id originalManagerId,s.original_manager_name originalManagerName,");
        sql.append("c.customer_area as customerArea from sales s ");
        sql.append("left join private_station c on s.customer_id = c.customer_id where 1=1 ");
        Map<String, Object> params = this.setQueryParams(condition, sql);
        sql.append("order by s.sales_date desc");
        Query query = this.entityManager.createNativeQuery(sql.toString());
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(SalesDTO.class));
        QueryUtils.setParams(query, params);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    @Override
    public int findCount(SalesDTO condition) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(s.id) from sales s ");
        sql.append("left join private_station c on s.customer_id = c.customer_id where 1=1 ");
        Map<String, Object> params = this.setQueryParams(condition, sql);
        Query query = this.entityManager.createNativeQuery(sql.toString());
        QueryUtils.setParams(query, params);
        return ((Number)query.getSingleResult()).intValue();
    }

    private Map<String, Object> setQueryParams(SalesDTO condition, StringBuilder sql) {
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
            /*if (StringUtils.isNotBlank(condition.getSalesStation())) {
                sql.append("and s.sales_station like :station ");
                params.put("station", "%" + condition.getSalesStation() + "%");
            }*/
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

    @Override
    public List<Sales> findByManagerSales(SalesDTO condition, int firstResult, int maxResults) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct id,sales_channel salesChannel,sales_date salesDate,");
        sql.append("customer_id customerId,customer_name customerName,sales_oil salesOil,");
        sql.append("manager_id managerId,manager_name managerName,");
        sql.append("sales_station salesStation,sales_count salesCount,sales_price salesPrice,");
        sql.append("is_transfer transfer,original_manager_id originalManagerId,original_manager_name originalManagerName ");
        sql.append("from ( ");
        sql.append("select distinct id,sales_channel,sales_date,customer_id,customer_name,sales_oil,");
        sql.append("manager_id,if(original_manager_name is not null, original_manager_name, manager_name) as manager_name,");
        sql.append("sales_station,sales_count,sales_price,is_transfer,original_manager_id,original_manager_name ");
        sql.append("from (select s.* from sales s ");
        sql.append("where s.customer_id <> '40257480' ");
        sql.append("and s.customer_id not in (select customer_id from customer where customer_manager = '大客户') ");
        sql.append("AND sales_channel <> '零售' ");
        sql.append("union all ");
        sql.append("select s.* from sales s where s.is_transfer = '1' and s.original_manager_name is not null ");
        sql.append(") s) s where 1=1 ");
        Map<String, Object> params = this.setQueryParams(condition, sql);
        sql.append("order by s.sales_date desc");

        Query query = this.entityManager.createNativeQuery(sql.toString());
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(SalesDTO.class));
        QueryUtils.setParams(query, params);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    @Override
    public int findCountByManagerSales(SalesDTO condition) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(distinct s.id) from (");
        sql.append("select distinct id,sales_channel,sales_date,customer_id,customer_name,sales_oil,");
        sql.append("manager_id,if(original_manager_name is not null, original_manager_name, manager_name) as manager_name,");
        sql.append("sales_station,sales_count,sales_price,is_transfer,original_manager_id,original_manager_name ");
        sql.append("from (");
        sql.append("select s.* from sales s ");
        sql.append("where s.customer_id <> '40257480' ");
        sql.append("and s.customer_id not in (select customer_id from customer where customer_manager = '大客户') ");
        sql.append("AND sales_channel <> '零售' ");
        sql.append("union all ");
        sql.append("select s.* from sales s where s.is_transfer = '1' and s.original_manager_name is not null ");
        sql.append(") s) s where 1=1 ");
        Map<String, Object> params = this.setQueryParams(condition, sql);
        sql.append("order by sales_date desc");

        Query query = this.entityManager.createNativeQuery(sql.toString());
        QueryUtils.setParams(query, params);
        return ((Number)query.getSingleResult()).intValue();
    }

    public List<Sales> findExact(Sales condition) {
        StringBuilder sql = new StringBuilder("select s from Sales s where 1=1 ");
        Map<String, Object> params = new HashMap<>();
        if (condition != null) {
            if (StringUtils.isNotBlank(condition.getCustomerId())) {
                sql.append("and customerId = :customerId ");
                params.put("customerId", condition.getCustomerId());
            }
            if (StringUtils.isNotBlank(condition.getSalesChannel())) {
                sql.append("and salesChannel = :salesChannel ");
                params.put("salesChannel", condition.getSalesChannel());
            }
            if (condition.getSalesDate() != null) {
                sql.append("and salesDate = :salesDate ");
                params.put("salesDate", condition.getSalesDate());
            }
            if (condition.getManagerId() != null) {
                sql.append("and managerId = :managerId ");
                params.put("managerId", condition.getManagerId());
            }
            if (condition.getSalesOil() != null) {
                sql.append("and salesOil = :salesOil ");
                params.put("salesOil", condition.getSalesOil());
            }
            if (condition.getSalesCount() != null) {
                sql.append("and salesCount = :salesCount ");
                params.put("salesCount", condition.getSalesCount());
            }
            if (condition.getSalesPrice() != null) {
                sql.append("and salesPrice = :salesPrice ");
                params.put("salesPrice", condition.getSalesPrice());
            }
            if (StringUtils.isNotBlank(condition.getSalesStation())) {
                sql.append("and sales_station = :salesStation ");
                params.put("salesStation", condition.getSalesStation());
            }
        }

        Query query = this.entityManager.createQuery(sql.toString());
        QueryUtils.setParams(query, params);
        return query.getResultList();
    }

    public List<Sales> findByNeedTransfer(SalesDTO condition) {
        StringBuilder sql = new StringBuilder();
        sql.append("select s.id,s.sales_channel salesChannel,s.sales_date salesDate,s.customer_id customerId,");
        sql.append("s.customer_name customerName,s.sales_oil salesOil,s.manager_id managerId,");
        sql.append("s.manager_name managerName,s.sales_station salesStation,");
        sql.append("s.sales_count salesCount,s.sales_price salesPrice,s.is_transfer transfer,");
        sql.append("s.original_manager_id originalManagerId,s.original_manager_name originalManagerName ");
        sql.append("from sales s ");
        sql.append("where s.sales_channel = '零售' and s.sales_station <> '#' ");
        sql.append("and s.customer_id in (");
        sql.append("select customer_id from sales where sales_channel <> '零售') ");

        Map<String, Object> params = this.setQueryParams(condition, sql);

        Query query = this.entityManager.createNativeQuery(sql.toString());
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(Sales.class));
        QueryUtils.setParams(query, params);
        return query.getResultList();
    }

    @Override
    public void save(Sales sales) {
        this.entityManager.persist(sales);
    }

    public void merge(Sales sales) {
        this.entityManager.merge(sales);
    }

    @Override
    public void delete(String id) {
        Sales instance = this.entityManager.find(Sales.class, id);
        this.entityManager.remove(instance);
    }
}
