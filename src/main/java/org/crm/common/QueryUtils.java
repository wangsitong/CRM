package org.crm.common;

import javax.persistence.Query;
import java.util.Map;

public class QueryUtils {

    public static void setParams(Query query, Map<String, Object> params) {
        if (!params.isEmpty()) {
            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
        }
    }

}
