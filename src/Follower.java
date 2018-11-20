import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import java.net.Socket;

import javax.net.ssl.*;


public class Follower {

    static Socket socket;
    static SSLSocket ssl_socket;
    static BufferedReader reader;
    static PrintWriter writer;

    static String SERVER_ADDRESS = "localhost"; // "192.168.43.177";

    static int PORT = 4444;
    static int SSL_PORT = 4443;

    static final String KEY_STORE_NAME =  "clientkeystore";
    static final String KEY_STORE_PASSWORD = "storepass";


    public static void main(String[] args){

        System.setProperty("javax.net.ssl.trustStore", KEY_STORE_NAME);
        System.setProperty("javax.net.ssl.trustStorePassword", KEY_STORE_PASSWORD);


        System.out.println("Enter TCP/SSL: ");
        Scanner sc = new Scanner(System.in);

        if (sc.nextLine().toLowerCase().startsWith("s"))
            initialize_ssl_connection();
        else initialize_connection();


        String cmd, resp;

        while (true) {

            System.out.println("Enter a command: ");
            cmd = sc.nextLine();

            if (cmd.equals("exit")) break;


            do {

                send_command(cmd);
                resp = get_response();

            } while (resp.equals(""));


            if (resp.contains("OK") && cmd.contains("submit"))
                System.out.println("Successfully submitted " + cmd.split(" ")[1] + ", " + cmd.split(" ")[2] + " to server at IP address of " + SERVER_ADDRESS);
            else System.out.println(resp);
        }


        Follower.terminate_connection();

    }


    static void initialize_ssl_connection() {

        boolean is_init = false;

        do {

            try {

                SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                ssl_socket = (SSLSocket) factory.createSocket(SERVER_ADDRESS, SSL_PORT);
                ssl_socket.startHandshake();

                writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(ssl_socket.getOutputStream())));
                reader = new BufferedReader(new InputStreamReader(ssl_socket.getInputStream()));
                is_init = true;

            } catch (Exception e) {
                System.out.println("Follower : Creation error.");
            }

            if (!is_init) {

                try {

                    Thread.sleep(5*1_000);

                } catch (InterruptedException e) {
                    System.out.println("Follower : Suspend error.");
                }
            }

        } while(!is_init);

        System.out.println("Follower : ssl_socket created.");
    }


    static void initialize_connection(){

        boolean is_init = false;

        do {

            try {

                socket = new Socket(SERVER_ADDRESS, PORT);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream());

                is_init = true;

            } catch (IOException e) {

                System.out.println("Follower : Creation error.");

            }

            if (!is_init) {

                try {

                    Thread.sleep(5*1_000);

                } catch (InterruptedException e) {
                    System.out.println("Follower : Suspend error.");
                }

            }

        } while(!is_init);

        System.out.println("Follower : socket is created.");
    }


    static void send_command(String cmd) {

        writer.println(cmd);
        writer.flush();

    }


    static String get_response() {

        final String[] response = {""};

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {

            try {

                response[0] = reader.readLine();

            } catch (IOException e) {
                System.out.println("Follower : Response error.");
            }

        });

        try {

            Thread.sleep(2_000);
            executor.shutdownNow();

            if (response[0].equals("")) {

                System.out.println("Follower : Re-sending message.");
            }

        } catch (InterruptedException e) {
            System.out.println("Follower : Thread sleep error.");
        }

        return response[0];
    }


    static void terminate_connection() {

        Follower.send_command("connection_terminate");

        try {

            if (socket!= null) {socket.close();}
            if (ssl_socket != null) {ssl_socket.close();}
            if (reader != null) {reader.close();}
            if (writer != null) {writer.close();}

        } catch (IOException e) {

            System.out.println("Follower : Termination error.");

        }

    }

}