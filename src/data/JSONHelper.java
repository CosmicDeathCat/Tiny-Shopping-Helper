package data;
import core.*;
import com.google.gson.*;
/**
 * This class contains methods for converting objects to and from JSON.
 */
public class JSONHelper {

    public static GsonBuilder builder = new GsonBuilder().setPrettyPrinting();

    /**
     * This method converts a ShoppingItem to JSON.
     * @param item
     * @return
     */
    public static String toJSON(ShoppingItem item) {
        Gson gson = builder.create();
        return gson.toJson(item);
    }

    /**
     * This method converts JSON to a ShoppingItem.
     * @param json
     * @return
     */
    public static ShoppingItem fromJSON(String json) {
        Gson gson = builder.create();
        return gson.fromJson(json, ShoppingItem.class);
    }

    /**
     * This method converts a ShoppingCart to JSON.
     * @param cart
     * @return
     */
    public static String toJSON(ShoppingCart cart) {
        Gson gson = builder.create();
        return gson.toJson(cart);
    }

    /**
     * This method converts JSON to a ShoppingCart.
     * @param json
     * @return
     */
    public static ShoppingCart fromJSONCart(String json) {
        Gson gson = builder.create();
        return gson.fromJson(json, ShoppingCart.class);
    }
}
