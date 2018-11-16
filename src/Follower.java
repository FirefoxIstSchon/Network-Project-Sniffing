

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import javax.net.ssl.*;
import java.io.FileInputStream;
import java.security.KeyStore;

public class Follower {

    Socket socket;
    SSLSocket ssl_socket;
    BufferedReader reader;
    PrintWriter writer;

        static String SERVER_ADDRESS = "localhost";
        static int SSL_PORT=4443;
        static int SERVER_PORT = 4444;
        static Follower follower;

        private final String KEY_STORE_NAME =  "clientkeystore";
        private final String KEY_STORE_PASSWORD = "storepass";

    public static void main(String[] args){


        Scanner sc=new Scanner(System.in);
        String connection_type="";
        System.out.println("Enter TCP/SSL: ");
        connection_type=sc.nextLine().toLowerCase();
        boolean is_valid=false;

        do{
            if(connection_type.equals("ssl")){
                follower = new Follower(SERVER_ADDRESS, SSL_PORT);
                follower.initialize_ssl_connection();
                is_valid=true;

            }else if (connection_type.equals("tcp")){
                follower = new Follower(SERVER_ADDRESS, SERVER_PORT);
                follower.initialize_connection();
                is_valid=true;
            }else{
                System.out.println("invalid connection type");
                System.out.println("Do you want an SSL connection or a TCP connection?");

            }
        }while(!is_valid);



        if (follower.socket == null && follower.ssl_socket == null) {

            System.out.println("Follower : connectivity is not established.");

        } else {

            System.out.println("Follower : connectivity is established.");

            // update with master

            new Thread(new Master_Connecter(follower, 10)).start();

            // follower.terminate_connection();

        }

    }

    public void initialize_ssl_connection()  {
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
                e.printStackTrace();
                System.out.println("Follower : Creation error.");
            }

            if (!is_init) {

                try {

                    Thread.sleep(5*1_000);

                } catch (InterruptedException e) {

                    System.out.println("Follower : Suspend error.");

                }

            }




        }while(!is_init);
    }


    public Follower(String SERVER_ADDRESS, int SERVER_PORT){
        this.SERVER_ADDRESS=SERVER_ADDRESS;
        this.SERVER_PORT=SERVER_PORT;
        System.setProperty("javax.net.ssl.trustStore", KEY_STORE_NAME);
        System.setProperty("javax.net.ssl.trustStorePassword", KEY_STORE_PASSWORD);

    }

    public  void initialize_connection(){

        boolean is_init = false;

        do {

            try {

                socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
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
    }

    public void send_command(String cmd) {

        writer.println(cmd);
        writer.flush();

    }


    public String get_response() {

        String response = "";

        try {

            response = reader.readLine();

        } catch (IOException e) {

            System.out.println("Follower : Response error.");

        }

        return response;

    }
    public void terminate_connection() {

        try {

            if (socket!= null) {socket.close();}
            if (reader != null) {reader.close();}
            if (writer != null) {writer.close();}

        } catch (IOException e) {

            System.out.println("Follower : Termination error.");

        }

    }


}





class Master_Connecter implements Runnable{

    Follower follower;
    int timeout_ms;

    public Master_Connecter(Follower follower, int timeout) {
        this.follower = follower;
        this.timeout_ms = timeout * 1_000;
    }

    @Override
    public void run() {

        String command="";
        String str="";

        Scanner sc=new Scanner(System.in);

        while (true) {

            System.out.println("Write down a command you want to send.");
            command=sc.nextLine();

            if(command.equals("exit")) {
                follower.send_command("connection_terminate");
                follower.terminate_connection();
                System.out.flush();
                break;
            }



            follower.send_command(command);
            str =follower.get_response();
            System.out.println(str);
            System.out.flush();



            }



            try {
                Thread.sleep(timeout_ms);
            } catch (InterruptedException e) {
                System.out.println("Master_Connector thread interrupted");
            }

        }

    }



