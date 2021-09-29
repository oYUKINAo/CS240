package handler;

import com.google.gson.Gson;

public class JsonSerializer {
    public JsonSerializer() {}

    /**
     * @param value JSon string
     * @param object Java object
     * @param <T> Type of object
     * @return JSon string converted to type T
     */
    public static <T> T deserialize(String value, Class<T> object) {
        return (new Gson()).fromJson(value, object);
    }

    /**
     * @param javaObject Java object
     * @param <T> Type of javaObject
     * @return JSon string converted from javaObject
     */
    public static <T> String serialize(T javaObject) {
        return new Gson().toJson(javaObject);
    }
}
