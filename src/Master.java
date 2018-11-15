import java.io.*;

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

    static String keystore = "keystore.jks";
    static String keystore_pass = "storepass";
    static String key_pass = "keypass";


    public static void main(String[] args){

        Database.prepare_database();
        Master.initialize_connection();

        new Thread(Master::listen_for_commands).start();
        new Thread(Master::listen_for_commands_encrypted).start();

    }


    static void initialize_connection(){

        try {

            serverSocket = new ServerSocket(server_port);

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


    static void listen_for_commands(){

        try {

            socket = serverSocket.accept();

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String cmd = reader.readLine(), response = "";


            while(!cmd.equals("connection_terminate")){

                System.out.println("Master received cmd: " + cmd);

                String[] cmd_args = cmd.split(" ");

                switch(cmd_args[0]){

                    case "submit":

                        response = Database.put(cmd_args[1], cmd_args[2]);
                        break;

                    case "get":

                        response = Database.get(cmd_args[1]);
                        break;

                }

                writer.write(response);
                writer.flush();

                cmd = reader.readLine();

            }

            try {

                reader.close();
                writer.close();

            } catch (Exception e) {
                System.out.println("Master reader/writer termination error :" + e.toString());
            }

            terminate_connection();

        } catch (IOException e) {
            System.out.println("Master socket error: " + e.toString());
        }
    }


    static void listen_for_commands_encrypted(){

        try {

            sslSocket = (SSLSocket) sslServerSocket.accept();

            BufferedReader reader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));

            String cmd = reader.readLine(), response = "";


            while(!cmd.equals("connection_terminate")){

                System.out.println("Master received cmd: " + cmd);

                String[] cmd_args = cmd.split(" ");

                switch(cmd_args[0]){

                    case "submit":

                        response = Database.put(cmd_args[1], cmd_args[2]);
                        break;

                    case "get":

                        response = Database.get(cmd_args[1]);
                        break;

                }

                writer.write(response);
                writer.flush();

                cmd = reader.readLine();

            }

            try {

                reader.close();
                writer.close();

            } catch (Exception e) {
                System.out.println("Master reader/writer termination error :" + e.toString());
            }

            terminate_connection();

        } catch (IOException e) {
            System.out.println("Master ssl-socket error: " + e.toString());
        }

    }


    static void terminate_connection(){

        try {

            if (serverSocket != null) serverSocket.close();
            if (sslServerSocket != null) sslServerSocket.close();

            Database.save_database();

        } catch (IOException e) {
            System.out.println("Master socket termination error: " + e.toString());
        }
    }

}
