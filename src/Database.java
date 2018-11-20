import java.io.*;
import java.util.HashMap;


class Database {

    static Database database;
    static HashMap<String, String> hashMap;


    Database(){

        hashMap = load_database();

    }


    static void prepare_database(){

        if (database == null) database = new Database();
        System.out.println("Database prepared.");

    }


    static String put(String key, String value){

        String msg;

        if (hashMap.get(key) == null){

            hashMap.put(key, value);
            msg = "OK." ;

        } else {

            hashMap.replace(key, value);
            msg = "OK." ;

        }

        return msg;
    }


    static String get(String key){

        String msg;

        if (hashMap.get(key) == null){

            msg = "No stored value for " + key + ".";

        } else {

            msg = hashMap.get(key);

        }

        return msg;
    }


    static void save_database(){

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


    static HashMap<String, String> load_database(){

        ObjectInputStream objectInputStream;

        HashMap hashMap;

        try {

            objectInputStream = new ObjectInputStream(new FileInputStream("hashmap.ser"));
            hashMap = (HashMap) objectInputStream.readObject();

        } catch (IOException e) {
            System.out.println("Database file not found.");
            hashMap = new HashMap<String, String>();
        } catch (ClassNotFoundException e) {
            System.out.println("Class conversion error: " + e.toString());
            hashMap = new HashMap<String, String>();
        }

        return hashMap;
    }

}
