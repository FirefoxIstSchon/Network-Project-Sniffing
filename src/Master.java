import java.io.*;
import java.util.HashMap;


public class Master {

    static Master master;

    static HashMap<String, String> database;


    public Master(){

        database = load_hashmap();

    }


    public static Master get_instance(){
        if (master == null) master = new Master();
        return master;
    }


    public static String client_put(String key, String value){

        String msg;

        if (database.get(key) == null){

            database.put(key, value);
            msg = "Put successful: Key " + key + " Value " + value + " added." ;

        } else {

            msg = "Put unsuccessful: Key " + key + " already exists.";

        }

        return msg;
    }


    public static String client_get(String key){

        String msg;

        if (database.get(key) == null){

            msg = "Get unsuccessful: Key " + key + " not found.";

        } else {

            msg = database.get(key);

        }

        return msg;
    }


    public static void save_hashmap(){

        ObjectOutputStream objectOutputStream;

        try {

            objectOutputStream = new ObjectOutputStream(new FileOutputStream("database.ser"));
            objectOutputStream.writeObject(database);
            objectOutputStream.close();

            System.out.println("Database saved in database.ser");

        } catch (IOException e) {
            System.out.println("Error saving database: " + e.toString());
        }
    }


    public static HashMap<String, String> load_hashmap(){

        ObjectInputStream objectInputStream;

        HashMap hashMap;

        try {

            objectInputStream = new ObjectInputStream(new FileInputStream("hashmap.ser"));
            hashMap = (HashMap) objectInputStream.readObject();

        } catch (IOException e) {
            System.out.println("Database file not found: " + e.toString());
            hashMap = new HashMap<String, String>();
        } catch (ClassNotFoundException e) {
            System.out.println("Class conversion error: " + e.toString());
            hashMap = new HashMap<String, String>();
        }

        return hashMap;
    }

}
