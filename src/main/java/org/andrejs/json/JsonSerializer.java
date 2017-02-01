package org.andrejs.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.fasterxml.jackson.core.JsonParser.Feature.*;

public class JsonSerializer {

	final static ObjectMapper serializer = new ObjectMapper()
		.configure(ALLOW_COMMENTS, true).configure(ALLOW_UNQUOTED_CONTROL_CHARS, true)
		.configure(ALLOW_SINGLE_QUOTES, true).configure(ALLOW_UNQUOTED_FIELD_NAMES, true);

	public static String toJsonString(Map<String, Object> val) {
		try {
			return serializer.writeValueAsString(val);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/** Get JSON with indentation **/
	public static String toPrettyString(Map<String, Object> val) {
		try {
			return serializer.writerWithDefaultPrettyPrinter().writeValueAsString(val);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> parse(String jsonString) {
		try {
			if( jsonString.isEmpty() ) return new LinkedHashMap<String, Object>();
			return serializer.readValue(jsonString, Map.class);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> serialize(Object pojoBean) {
		try {
			return serializer.convertValue(pojoBean, Map.class);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public static Object getFieldValue(Field f, Object o) {
		try {
			f.setAccessible(true);
			return f.get(o);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}
}
