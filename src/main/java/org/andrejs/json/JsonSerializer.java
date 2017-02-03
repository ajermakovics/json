package org.andrejs.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.fasterxml.jackson.core.JsonParser.Feature.*;

class JsonSerializer {

	final static ObjectMapper serializer = new ObjectMapper()
		.configure(ALLOW_COMMENTS, true).configure(ALLOW_UNQUOTED_CONTROL_CHARS, true)
		.configure(ALLOW_SINGLE_QUOTES, true).configure(ALLOW_UNQUOTED_FIELD_NAMES, true);

	static String toJsonString(Map<String, Object> val) {
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
	static Map<String, Object> parse(String jsonString) {
		try {
			if( jsonString.isEmpty() ) return new LinkedHashMap<String, Object>();
			return serializer.readValue(jsonString, Map.class);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	static Map<String, Object> readUrl(String url) {
		try {
			return serializer.readValue(new URL(url), Map.class);
		} catch (IOException e) {
			throw new IOError(e);
		}
	}

	static Map<String, Object> readFile(String filePath) {
		try {
			return serializer.readValue(new File(filePath), Map.class);
		} catch (IOException e) {
			throw new IOError(e);
		}
	}

	static Map<String, Object> readBytes(byte[] json) {
		try {
			return serializer.readValue(json, Map.class);
		} catch (IOException e) {
			throw new IOError(e);
		}
	}

	static Map<String, Object> readStream(InputStream json) {
		try {
			return serializer.readValue(json, Map.class);
		} catch (IOException e) {
			throw new IOError(e);
		}
	}

	@SuppressWarnings("unchecked")
	static Map<String, Object> serialize(Object pojoBean) {
		try {
			return serializer.convertValue(pojoBean, Map.class);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	static Object getFieldValue(Field f, Object o) {
		try {
			f.setAccessible(true);
			return f.get(o);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}
}
