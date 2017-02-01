package org.andrejs.json;

import org.junit.Before;
import org.junit.Test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class JsonScriptingTest {

	ScriptEngine eng;
	Invocable inv;

	@Before
	public void getNashorn() {
		eng = new ScriptEngineManager().getEngineByName("Nashorn");
		assertNotNull("Nashorn javascript engine not found", eng);
		inv = (Invocable) eng;
	}

	@Test
	public void usableInScriptEngineAsBindings() throws Exception {

		Json j = new Json("a", 5);

		Object result = eng.eval("a+1", j);

		assertEquals(6, result);
	}

	@Test
	public void usableAsFunctionArgument() throws Exception {

		Json j = new Json("a", 5);

		eng.eval("function inc(j){ j.b = j.a+1; }");

		inv.invokeFunction("inc", j);

		assertEquals(6, j.get("b", 0).intValue());
	}

	@Test
	public void modifiableAsFunctionArgument() throws Exception {

		Json addr = new Json("port", 80);

		eng.eval("function nextPort(addr) { addr.port++; }");

		inv.invokeFunction("nextPort", addr);

		assertEquals(81, addr.get("port", 0.0).intValue());
	}

	@Test
	public void canIterateKeysInJs() throws Exception {

		Json j = new Json("a", 5).set("b", 6);
		Json j2 = new Json();

		eng.eval("function it(j, j2){ for(key in j) j2[key] = j[key]; }");

		inv.invokeFunction("it", j, j2);

		assertEquals(j, j2);
	}
}
