package org.crm.common;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtils {

    public static final String MESSAGE_SUCCESS = "success";
    public static final String MESSAGE_ERROR = "error";

    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_ERROR = -1;

    public static Map<String, Object> getResult(int status, Object data, PageInfo pageInfo, String message) {
        Map<String, Object> result = new HashMap<>();
        if (data != null) {
            if (data instanceof Map) {
                Map<String, Object> resultMap = (Map<String, Object>) data;
                if (!resultMap.containsKey("data")) {
                    result.put("data", data);
                } else {
                    result = resultMap;
                }
            } else {
                result.put("data", data);
            }
        }
        if (pageInfo != null) {
            result.put("page", pageInfo);
        }
        if (message != null) {
            result.put("message", message);
        }

        result.put("status", status);
        return result;
    }

    public static Map<String, Object> getResult(int status, Object data) {
        return getResult(status, data, null, null);
    }

    public static Map<String, Object> getResult(int status, Object data, PageInfo pageInfo) {
        return getResult(status, data, pageInfo, null);
    }

    public static Map<String, Object> getResult(int status, PageDTO pageDTO) {
        Map<String, Object> result = new HashMap<>();
        result.put("data", pageDTO.getDataList());
        result.put("total", pageDTO.getTotal());
        return getResult(status, result, null, null);
    }

    public static Map<String, Object> getResult(int status, String message) {
        return getResult(status, null, null, message);
    }

    public static Object success() {
        return getResult(STATUS_SUCCESS, MESSAGE_SUCCESS);
    }

    public static Object error(String message) {
        return getResult(STATUS_ERROR, StringUtils.defaultIfBlank(message, MESSAGE_ERROR));
    }

}
