package org.andrejs.json;

import javax.script.Bindings;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/** {@link Bindings} that delegate operations to a {@link Map} **/
public abstract class MapBindings implements Bindings {

	public abstract Map<String, Object> toMap();

	@Override
	public int size() {
		return toMap().size();
	}

	@Override
	public boolean isEmpty() {
		return toMap().isEmpty();
	}

	@Override
	public boolean containsValue(Object value) {
		return toMap().containsValue(value);
	}

	@Override
	public void clear() {
		toMap().clear();
	}

	@Override
	public Set<String> keySet() {
		return toMap().keySet();
	}

	@Override
	public Collection<Object> values() {
		return toMap().values();
	}

	@Override
	public Set<Map.Entry<String, Object>> entrySet() {
		return toMap().entrySet();
	}

	@Override
	public Object put(String name, Object value) {
		return toMap().put(name, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> toMerge) {
		toMap().putAll(toMerge);
	}

	@Override
	public boolean containsKey(Object key) {
		return toMap().containsKey(key);
	}

	@Override
	@Deprecated
	public Object get(Object key) {
		return toMap().get(key);
	}

	@Override
	public Object remove(Object key) {
		return toMap().get(key);
	}
}
