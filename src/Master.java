import java.io.*;
import java.util.Scanner;

import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ssl.*;
import java.security.*;
import java.security.cert.CertificateException;


class Master {

    static int server_port = 4444;
    static int server_port_ssl = 4443;

    static ServerSocket serverSocket;
    static Socket socket;

    static SSLServerSocket sslServerSocket;
    static SSLSocket sslSocket;

    static final String keystore = "keystore.jks";
    static final String keystore_pass = "storepass";
    static final String key_pass = "keypass";

    static boolean is_sql = false;


    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);


        System.out.println("Enter Hashmap/SQL: ");
        if (sc.nextLine().toLowerCase().startsWith("s")) is_sql = true;


        if (!is_sql) Database.prepare_database();
        else SQL_Database.prepare_database();


        System.out.println("Enter TCP/SSL: ");

        if (sc.nextLine().toLowerCase().startsWith("s")) {
            initialize_connection_ssl();
            new Thread(new Runnable() {
                @Override

                public void run() {
                    listen_for_commands_ssl(this);
                }
            }).start();
        }
        else {
            initialize_connection();
            new Thread(new Runnable() {
                @Override

                public void run() {
                    listen_for_commands(this);
                }
            }).start();
        }
    }


    static void initialize_connection(){

        try {

            serverSocket = new ServerSocket(server_port);

        } catch (IOException e) {
            System.out.println("Master creation error: " + e.toString());
        }
    }


    static void initialize_connection_ssl(){

        try {

            SSLContext sc = SSLContext.getInstance("TLS");
            char ksPass[] = keystore_pass.toCharArray();
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(keystore), ksPass);
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, key_pass.toCharArray());
            sc.init(kmf.getKeyManagers(), null, null);

            sslServerSocket = (SSLServerSocket) sc.getServerSocketFactory().createServerSocket(server_port_ssl);

        } catch (IOException e) {
            System.out.println("Master creation error: " + e.toString());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Master SSL error: " + e.toString());
        } catch (CertificateException e) {
            System.out.println("Master certificate error: " + e.toString());
        } catch (UnrecoverableKeyException | KeyStoreException | KeyManagementException e) {
            System.out.println("Master encryption error: " + e.toString());
        }
    }


    static void listen_for_commands(Runnable r){

        try {

            socket = serverSocket.accept();
            while (socket == null) Thread.sleep(300);

            System.out.println("Follower Joined on tcp.");
            new Thread(r).start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            String cmd = reader.readLine(), response = "";


            while(!(cmd == null || cmd.equals("connection_terminate"))){

                System.out.println("Master received cmd: " + cmd);

                String[] cmd_args = cmd.split(" ");

                switch(cmd_args[0]){

                    case "submit":

                        if (!is_sql) response = Database.put(cmd_args[1], cmd_args[2]);
                        else response = SQL_Database.put(cmd_args[1], cmd_args[2]);
                        break;

                    case "get":

                        if (!is_sql) response = Database.get(cmd_args[1]);
                        else response = SQL_Database.get(cmd_args[1]);
                        break;

                    default: response = "Your command '"+cmd+"' is invalid.";

                }

                writer.println(response);
                writer.flush();

                cmd = reader.readLine();

            }

            System.out.println("Follower Exited from tcp.");

            try {

                reader.close();
                writer.close();

            } catch (Exception e) {
                System.out.println("Master reader/writer termination error :" + e.toString());
            }

        } catch (IOException e) {
            System.out.println("Master socket error: " + e.toString());
        } catch (InterruptedException e) {
            System.out.println("Master thread sleep error: " + e.toString());
        }


        if (!is_sql) save_database();
    }


    static void listen_for_commands_ssl(Runnable r){

        try {

            sslSocket = (SSLSocket) sslServerSocket.accept();
            while (sslSocket == null) Thread.sleep(300);

            System.out.println("Follower Joined on ssl.");
            new Thread(r).start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(sslSocket.getOutputStream()));

            String cmd = reader.readLine(), response = "";


            while(!cmd.equals("connection_terminate")){

                System.out.println("Master received cmd: " + cmd);

                String[] cmd_args = cmd.split(" ");

                switch(cmd_args[0]){

                    case "submit":

                        if (!is_sql) response = Database.put(cmd_args[1], cmd_args[2]);
                        else response = SQL_Database.put(cmd_args[1], cmd_args[2]);
                        break;

                    case "get":

                        if (!is_sql) response = Database.get(cmd_args[1]);
                        else response = SQL_Database.get(cmd_args[1]);
                        break;

                    default: response = "Your command '"+cmd+"' is invalid.";

                }

                writer.println(response);
                writer.flush();

                cmd = reader.readLine();

            }

            System.out.println("Follower Exited from ssl.");

            try {

                reader.close();
                writer.close();

            } catch (Exception e) {
                System.out.println("Master reader/writer termination error :" + e.toString());
            }

        } catch (IOException e) {
            System.out.println("Master ssl_socket error: " + e.toString());
        } catch (InterruptedException e) {
            System.out.println("Master thread sleep error: " + e.toString());
        }


        if (!is_sql) save_database();
    }


    static void save_database(){

        Database.save_database();
    }

}
