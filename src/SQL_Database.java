import java.io.*;
import java.util.HashMap;

import com.mysql.jdbc.Driver;
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

        String return_msg;

        try {

            statement = connection.createStatement();



            return_msg = "OK.";

        } catch (SQLException e) {
            System.out.println("Database SQL error: " + e.toString());
            return_msg = "Not OK.";
        }


        return return_msg;
    }


//    static String get(String key){
//
//        String msg;
//
//
//
//
//
//
//        return msg;
//    }

    public static void main(String[] args){

        prepare_database();



    }

}
