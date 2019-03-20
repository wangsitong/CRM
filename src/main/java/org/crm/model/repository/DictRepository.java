package org.crm.model.repository;

import org.crm.model.entity.Dict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DictRepository extends JpaRepository<Dict, String> {

    @Query("select max(key) from Dict d where d.parent = ?1")
    String findMaxKey(String parent);

    List<Dict> findByParentOrderBySortNoAsc(String parent);

    void deleteByParent(String parent);

}
