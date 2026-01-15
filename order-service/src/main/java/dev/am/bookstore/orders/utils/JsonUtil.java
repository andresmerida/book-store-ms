package dev.am.bookstore.orders.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import dev.am.bookstore.orders.web.exceptions.JsonMappingObjException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JsonUtil {

    private JsonUtil() {}

    public static String toJson(JsonMapper jsonMapper, Object obj) {
        try {
            return jsonMapper.writeValueAsString(obj);
        } catch (JacksonException e) {
            throw new JsonMappingObjException(
                    "Failed toJson mapping object of type: "
                            + (obj == null ? "null" : obj.getClass().getName()),
                    e);
        }
    }

    public static <T> T fromJson(JsonMapper jsonMapper, String json, Class<T> clazz) {
        try {
            return jsonMapper.readValue(json, clazz);
        } catch (JacksonException e) {
            throw new JsonMappingObjException("Failed fromJson mapping class of type: " + clazz.getName(), e);
        }
    }
}
