import java.io.*;
import java.util.HashMap;


public class Database {

    static Database database;

    static HashMap<String, String> hashMap;


    public Database(){

        hashMap = load_hashmap();

    }


    public static Database get_instance(){
        if (database == null) database = new Database();
        return database;
    }


    public static String client_put(String key, String value){

        String msg;

        if (hashMap.get(key) == null){

            hashMap.put(key, value);
            msg = "Put successful: Key " + key + " Value " + value + " added." ;

        } else {

            msg = "Put unsuccessful: Key " + key + " already exists.";

        }

        return msg;
    }


    public static String client_get(String key){

        String msg;

        if (hashMap.get(key) == null){

            msg = "Get unsuccessful: Key " + key + " not found.";

        } else {

            msg = hashMap.get(key);

        }

        return msg;
    }


    public static void save_hashmap(){

        ObjectOutputStream objectOutputStream;

        try {

            objectOutputStream = new ObjectOutputStream(new FileOutputStream("hashMap.ser"));
            objectOutputStream.writeObject(hashMap);
            objectOutputStream.close();

            System.out.println("Database saved in hashMap.ser");

        } catch (IOException e) {
            System.out.println("Error saving hashMap: " + e.toString());
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
