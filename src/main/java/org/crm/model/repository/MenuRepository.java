package org.crm.model.repository;

import org.crm.model.entity.Menu;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

// @CacheConfig(cacheNames = "menus")
public interface MenuRepository extends CrudRepository<Menu, String> {

    // @Cacheable
    List<Menu> findByParentNoOrderBySortNoAsc(String parentNo);

}
