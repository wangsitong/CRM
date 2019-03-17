package org.crm.common;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtils {

    public static final String MESSAGE_SUCCESS = "success";
    public static final String MESSAGE_ERROR = "error";

    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_ERROR = -1;

    public static Map<String, Object> getResult(int status, Object data, String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("status", status);
        if (data != null) {
            result.put("data", data);
        }
        if (message != null) {
            result.put("message", message);
        }
        return result;
    }

    public static Map<String, Object> getResult(int status, Object data) {
        return getResult(status, data, null);
    }

    public static Map<String, Object> getResult(int status, String message) {
        return getResult(status, null, message);
    }

    public static Object success() {
        return getResult(STATUS_SUCCESS, MESSAGE_SUCCESS);
    }

    public static Object error(String message) {
        return getResult(STATUS_ERROR, StringUtils.defaultIfBlank(message, MESSAGE_ERROR));
    }

}
