package org.crm.model.repository.impl;

import org.crm.model.repository.DemandRepository;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Repository
public class DemandRepositoryImpl implements DemandRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Map<String, Object>> getAreas(String year) {
        StringBuilder sql = new StringBuilder();
        sql.append("select c.customer_area as area,");
        sql.append("sum(d.customer_demand_gas) as plan_gas, sum(d.customer_demand_diesel) as plan_diesel ");
        sql.append("from customer c left join customer_demand d on c.customer_id = d.customer_id ");
        sql.append("where 1=1 ");
        sql.append("and c.customer_id in (select customer_id from private_station) ");
        sql.append("and d.customer_demand_year = :year ");
        sql.append("group by c.customer_area ");

        Query query = this.entityManager.createNativeQuery(sql.toString());
        query.setParameter("year", year);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

}
