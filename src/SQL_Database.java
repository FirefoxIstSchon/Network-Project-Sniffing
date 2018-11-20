// import com.mysql.jdbc.Driver;
import java.sql.*;


public class SQL_Database {

    static Connection connection;

    static String query;
    static Statement statement;
    static ResultSet resultSet;


    static void prepare_database(){

        if (connection == null) try {

            Class.forName("com.mysql.jdbc.Driver");

            connection = DriverManager.getConnection(
                    "jdbc:mysql://sql2.freesqldatabase.com:3306/sql2266284",
                    "sql2266284", "pK8!vF7*");

            System.out.println("Connection successful.");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Connection failed. " + e.toString());
        }
    }


    static String put(String key, String value){

        String msg;

        try {

            statement = connection.createStatement();

            String is_existing = null;
            query = "SELECT * FROM userdata WHERE userkey = '"+key+"'";
            resultSet = statement.executeQuery(query);

            while(resultSet.next()){

                is_existing = resultSet.getString("uservalue");

            }

            if (is_existing == null) query = "INSERT INTO userdata (`userkey`,`uservalue`) VALUES ('"+key+"','"+value+"')";
            else query = "REPLACE INTO userdata (`userkey`,`uservalue`) VALUES ('"+key+"','"+value+"')";

            statement.executeUpdate(query);
            msg = "OK.";


        } catch (SQLException e) {
            System.out.println("Database SQL error: " + e.toString());
            msg = "Not OK.";
        }

        return msg;
    }


    static String get(String key){

        String msg;

        try {

            statement = connection.createStatement();

            String value = null;
            query = "SELECT * FROM userdata WHERE userkey = '"+key+"'";
            resultSet = statement.executeQuery(query);

            while(resultSet.next()){

                value = resultSet.getString("uservalue");

            }

            if (value == null) msg = "No stored value for " + key + ".";
            else msg = value;

        } catch (SQLException e) {
            System.out.println("Database SQL error: " + e.toString());
            msg = "Not OK.";
        }

        return msg;
    }

}
