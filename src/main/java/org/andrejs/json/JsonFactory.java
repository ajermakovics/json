package org.andrejs.json;


import java.io.InputStream;
import java.util.Map;

class JsonFactory {

    public Json map(Map<String, Object> map) {
        return new Json(map);
    }

    public Json string(String jsonString) {
        return new Json(JsonSerializer.parse(jsonString));
    }

    public Json bean(Object pojoBean) {
        return new Json(JsonSerializer.serialize(pojoBean));
    }

    public Json url(String url) {
        return new Json(JsonSerializer.readUrl(url));
    }

    public Json file(String filePath) {
        return new Json(JsonSerializer.readFile(filePath));
    }

    public Json bytes(byte[] bytes) {
        return new Json(JsonSerializer.readBytes(bytes));
    }

    public Json stream(InputStream stream) {
        return new Json(JsonSerializer.readStream(stream));
    }

    public Json keyVal(String key, Object val, Object ... keysAndVals) {
        Json json = new Json(key, val);
        for(int i = 0; i < keysAndVals.length; i++) {
            json.set(keysAndVals[i].toString(), keysAndVals[++i]);
        }
        return json;
    }
}
