package org.crm.service;

import org.crm.common.ConstantUtils;
import org.crm.model.entity.Menu;
import org.crm.model.entity.Role;
import org.crm.model.entity.UserRole;
import org.crm.model.repository.MenuRepository;
import org.crm.model.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    public List<Menu> getMenus() {
        List<Menu> roots = this.menuRepository.findByParentNoOrderBySortNoAsc(ConstantUtils.MENU_ROOT_NO);
        for (Menu menu : roots) {
            List<Menu> children = this.getMenus(menu.getMenuNo());
            if (children != null) {
                menu.setChildren(children);
            }
        }
        return roots;
    }

    public List<Menu> getMenus(String parentNo) {
        List<Menu> menus = this.menuRepository.findByParentNoOrderBySortNoAsc(parentNo);
        if (menus != null && !menus.isEmpty()) {
            for (Menu menu : menus) {
                List<Menu> children = this.getMenus(menu.getMenuNo());
                if (children != null) {
                    menu.setChildren(children);
                }
            }
        }

        return menus;
    }

}
