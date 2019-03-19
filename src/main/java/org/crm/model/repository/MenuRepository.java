package org.crm.model.repository;

import org.crm.model.entity.Menu;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

// @CacheConfig(cacheNames = "menus")
public interface MenuRepository extends CrudRepository<Menu, String> {

    // @Cacheable
    List<Menu> findByParentNoOrderBySortNoAsc(String parentNo);

}
