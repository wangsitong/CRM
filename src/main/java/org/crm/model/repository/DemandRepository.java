package org.crm.model.repository;

import java.util.List;
import java.util.Map;

public interface DemandRepository {

    List<Map<String, Object>> getAreas(String year);

}
