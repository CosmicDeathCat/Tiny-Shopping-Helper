package utility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

/**
 * This class contains methods for converting objects to and from JSON using generics.
 */
public class JSONHelper {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Converts an object of any type to JSON.
     * @param object The object to be converted to JSON.
     * @param <T> The type of the object.
     * @return A JSON string representing the object.
     */
    public static <T> String toJSON(T object) {
        return gson.toJson(object);
    }

    /**
     * Converts a JSON string to an object of any type.
     * @param json The JSON string to be converted.
     * @param object The class of T.
     * @param <T> The type of the object.
     * @return An object of type T.
     */
    public static <T> T fromJSON(String json, Class<T> object) {
        return gson.fromJson(json, object);
    }

    /**
     * Converts a list of any type to JSON.
     * @param list The list to be converted to JSON.
     * @param <T> The type of the objects in the list.
     * @return A JSON string representing the list.
     */
    public static <T> String toJSONList(List<T> list) {
        return gson.toJson(list);
    }

    /**
     * Converts a JSON string to a List of objects of any type.
     * @param json The JSON string to be converted.
     * @param object The class of objects in the list.
     * @param <T> The type of objects in the list.
     * @return A List of objects of type T.
     */
    public static <T> List<T> fromJSONList(String json, Class<T> object) {
        Type type = TypeToken.getParameterized(List.class, object).getType();
        return gson.fromJson(json, type);
    }
}
