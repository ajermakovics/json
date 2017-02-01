package org.andrejs.json;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.junit.Assert.*;


public class JsonTest {

	@Test
	public void newJsonIsEmpty() throws Exception {
		assertTrue( new Json().isEmpty() );
		assertEquals(Json.EMPTY, new Json());
	}

	@Test
	public void containsConstructorKeyValue() throws Exception {
		Json json = new Json("key", 123);
		assertTrue( json.containsKey("key") );
		assertEquals(123, json.get("key"));
	}

	@Test
	public void containsConstructorMap() throws Exception {
		Object val = 123;
		Map<String, Object> map = Collections.singletonMap("key", val);
		Json json = new Json( map );
		assertTrue( json.containsKey("key") );
		assertEquals(val, json.get("key"));
	}

	@Test
	@SuppressWarnings("unused")
	public void containsAnonymousClassFields() throws Exception {
		Json json = new Json() {
			int key = 123;
			String key2 = "val";
		};
		assertEquals(123, json.get("key"));
		assertEquals("val", json.get("key2"));
	}

	@Test
	public void createdWithHasValues() throws Exception {
		assertEquals(new Json("key", 123), new Json("{key: 123}"));
	}

	@Test
	public void listPropertyProducesArrayInJson() throws Exception {
		Json json = new Json("key", Arrays.asList(1, 2));
		assertEquals("{\"key\":[1,2]}", json.toString());
	}

	@Test
	public void toStringGiveJson() throws Exception {
		Json json = new Json("key", 123);
		assertEquals("{\"key\":123}", json.toString());
	}

	@Test
	public void twoJsonsWithSamePropertiesAreEqual() throws Exception {
		Json json = new Json("key", 123).set("key2", "val").set("n", new Json("a", 1));
		Json json2 = new Json("key2", "val").set("key", 123).set("n", new Json("a", 1));

		assertEquals( json, json2 );
		assertEquals( json.hashCode(), json2.hashCode() );
	}

	@Test
	public void getReturnsDefaultValueForMissingKey() throws Exception {
		assertEquals("val", new Json().get("key", "val") );

		assertEquals("val", new Json("key", "val").get("key", "val2") );
	}

	@Test
	public void atReturnsNestedJson() throws Exception {
		Json j = new Json("a", new Json("b", new Json("c", 1)));

		assertEquals(new Json("c", 1), j.at("a", "b"));
		assertTrue( j.at("a", "c").isEmpty() );
		assertEquals(Json.EMPTY, j.at("a", "c") );

	}

	@Test
	public void getWithDefaultValueReturnsNestedJson() throws Exception {
		Json j = new Json("a", new Json("b", 1));

		assertEquals(new Json("b", 1), j.get("a", Json.EMPTY));
		assertEquals( singletonMap("b", 1), j.get("a") );
		assertEquals(Json.EMPTY, j.get("missing", Json.EMPTY));
	}

	@Test
	public void mergeProducesCombinedJson() throws Exception {
		Json j1 = new Json("a", new Json("b", 1)).set("c", 1);
		Json j2 = new Json("a", new Json("b", 2)).set("d", 1);

		Json expected = new Json("a", new Json("b", 2)).set("c", 1).set("d", 1);

		assertEquals(expected, j1.merge(j2));
		j2.set("e", 1);
		assertEquals(expected, j1);
	}

	@Test
	public void copyMakesDeepCopy() throws Exception {

		Json j1 = new Json("a", new Json("b", 1));
		Json copy = j1.copy();

		assertEquals(j1, copy);
		copy.set("c", 1);
		assertNotSame(j1, copy);
	}
}
