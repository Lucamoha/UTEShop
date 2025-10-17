package com.uteshop.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.LinkedHashMap;
import java.util.Map;

public final class JsonUtil {
    public static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    public static String toJson(Object obj) {
        try { return MAPPER.writeValueAsString(obj); }
        catch (Exception e) { throw new RuntimeException("JSON serialize failed", e); }
    }

    public static Map<String,String> toFlatStringMap(String json) {
        try {
            Map<String, Object> any = MAPPER.readValue(json, new TypeReference<>() {});
            Map<String,String> out = new LinkedHashMap<>();
            flatten("", any, out);
            return out;
        } catch (Exception e) {
            throw new RuntimeException("JSON parse failed", e);
        }
    }

    public static <T> T toObject(String json, Class<T> cls) {
        try { return MAPPER.readValue(json, cls); }
        catch (Exception e) { throw new RuntimeException("JSON parse failed", e); }
    }

    private static void flatten(String prefix, Object val, Map<String,String> out) {
        if (val == null) { out.put(prefix, null); return; }
        if (val instanceof Map<?,?> m) {
            for (var e : m.entrySet()) {
                String k = String.valueOf(e.getKey());
                flatten(prefix.isEmpty()? k : prefix + "." + k, e.getValue(), out);
            }
        } else if (val instanceof Iterable<?> it) {
            int i = 0; for (Object v: it) flatten(prefix + "[" + (i++) + "]", v, out);
        } else {
            out.put(prefix, String.valueOf(val));
        }
    }
}
