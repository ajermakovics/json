package org.andrejs.json;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.lang.reflect.Modifier.isStatic;
import static java.util.stream.Collectors.toList;

/** JSON Object **/
public class Json extends MapBindings {

	/** Empty Json object. Immutable **/
	public static final Json EMPTY = new Json(MapOps.EMPTY_MAP);
	public static JsonFactory of = new JsonFactory();

	/** Map where all the properties are stored. Nested Json objects are stored as Maps **/
	Map<String, Object> map;

	/** Construct empty JSON Object **/
	public Json() {
	}

	/**  Construct a JSON Object by parsing a string **/
	public Json(String jsonString) {
		map = JsonSerializer.parse(jsonString);
	}

	/**  Construct a JSON Object from a map. Also usable as a copy constructor **/
	public Json(Map<String, Object> properties) {
		map = unwrap(properties);
	}

	/**  Construct a JSON Object with single key and value **/
	public Json(String key, Object value) {
		set(key, value);
	}

	@Override
	/** Get the underlying map that stores keys and values **/
	public Map<String, Object> toMap() {
		if( map == null )
			map = readFields();

		return map;
	}

	public boolean hasOwnProperty(String key) {
		return containsKey(key);
	}

	@Override
	/** Convert to JSON string representation **/
	public String toString() {
		return JsonSerializer.toJsonString(toMap());
	}

	/** Convert to pretty JSON string with indentation **/
	public String toStringPretty() {
		return JsonSerializer.toPrettyString(map);
	}

	@Override
	public boolean equals(Object it) {
		return (it instanceof Json) && toMap().equals( ((Json)it).toMap() );
	}

	@Override
	public int hashCode() {
		return toMap().hashCode();
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		return (T) toMap().get(key);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	/** Get a value or a default value **/
	public <T> T get(String key, T defaultValue) {
		Object v = toMap().get(key);
		if( defaultValue instanceof Json && v instanceof Map ) v = new Json( (Map) v );
		return v==null ? defaultValue : (T) v;
	}

	/** Same as {@link #put(String, Object)} but returns itself for chaining **/
	public Json set(String key, Object val) {
		put(key, val);
		return this;
	}

	@Override
	public Object put(String key, Object value) {
		return toMap().put(key, unwrap(value));
	}

	public Json putArray(String arrayField, List<?> values) {
		List<Object> vals = values.stream().map(Json::unwrap).collect(toList());
		put(arrayField, vals);
		return this;
	}

	@SuppressWarnings("unchecked")
	/** Get the map out of Json. Store nested Json objects as Maps **/
	static <T> T unwrap(T val) {
		if( val instanceof Json ) return (T) ((Json) val).toMap();
		return val;
	}

	/** Merge other json into this one recursively. Overwrites values from other if they exist in this.
	 * @return this
	 **/
	public Json merge(Json other) {
		MapOps.mergeMaps(toMap(), other.toMap());
		return this;
	}

	/**
	 * Get nested JSON object
	 * @param keys path to nested object
	 * @return nested JSON object or {@link #EMPTY} JSON object if nothing found
	 */
	public Json at(String ... keys) {
		Map<String, Object> nested = MapOps.getNested(toMap(), keys);
		return (nested == MapOps.EMPTY_MAP) ? EMPTY : new Json(nested);
	}

	public List<Object> getArray(String key) {
		return get(key);
	}

	public List<Json> getObjects(String key) {
		List<Object> array = getArray(key);
		if(array == null)
			return null;
		return array.stream()
				.map(Json.of::bean)
				.collect(toList());
	}

	/** Create a (deep) copy of this Json. **/
	public Json copy() {
		return new Json( MapOps.deepCopyMap(toMap()) );
	}

	/** Read declared field values using reflection **/
	Map<String, Object> readFields() {
		Map<String, Object> fieldVals = new LinkedHashMap<>();
		for(Field f: getClass().getDeclaredFields()) {
			if(f.getName().contains("$"))
				continue;
			Object val = JsonSerializer.getFieldValue(f, this);
			if( val != null && !isStatic(f.getModifiers()) )
				fieldVals.put( f.getName() , val);
		}
		return fieldVals;
	}

}

