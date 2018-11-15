import java.util.HashMap;
import java.util.Map;

public class Master {

    static Master master;

    static Map<String, String> database;

    public Master(){

        // todo : load from disk here.
        // if not found.
        database = new HashMap<>();

        // also save to disk when done.

    }

    public static Master get_instance(){
        if (master == null) master = new Master();
        return master;
    }

    public static String client_put(String key, String value){

        String msg = "";

        if (database.get(key) == null){

            database.put(key, value);
            msg = "Put successful: Key" ;

        } else {

            msg = "Put unsuccessful: Key " + key + " already exists.";

        }


        return msg;
    }

    public static String client_get(String key){

        String msg = "";


        return msg;
    }

}
