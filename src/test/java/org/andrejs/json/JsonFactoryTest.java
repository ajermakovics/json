package org.andrejs.json;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;

public class JsonFactoryTest {

    JsonFactory it = JsonFactory.INSTANCE;

    @Test
    public void map() throws Exception {
        assertEquals(new Json("key", "val"), it.map(singletonMap("key", "val")));
    }

    @Test
    public void string() throws Exception {
        assertEquals(new Json("key", "val"), it.string("{key: \"val\"}"));
    }

    @Test
    public void bean() throws Exception {
        assertEquals(new Json("key", "val"), it.bean(new Bean()));
    }

    @Test
    public void file() throws Exception {
        Path tempFile = Files.createTempFile("json", "test");
        Files.write(tempFile, "{key: \"val\"}".getBytes());

        assertEquals(new Json("key", "val"), it.file(tempFile.toString()));
    }

    @Test
    public void url() throws Exception {
        Path tempFile = Files.createTempFile("json", "test");
        Files.write(tempFile, "{key: \"val\"}".getBytes());

        assertEquals(new Json("key", "val"), it.url("file://" + tempFile.toString()));
    }


    @Test
    public void bytes() throws Exception {
        assertEquals(new Json("key", "val"), it.bytes("{key: \"val\"}".getBytes()));
    }

    @Test
    public void stream() throws Exception {
        InputStream stream = new ByteArrayInputStream("{key: \"val\"}".getBytes());
        assertEquals(new Json("key", "val"), it.stream(stream));
    }

    @Test
    public void keyVal() throws Exception {
        assertEquals(new Json("key", "val").set("key2", 123), it.keyVal("key", "val", "key2", 123));
    }

    public static class Bean {
        public String key = "val";
    }
}