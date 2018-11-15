import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;


class Master {

    static int server_port = 4444;
    static ServerSocket serverSocket;
    static Socket socket;


    static void main(String[] args){

        Database.prepare_database();
        Master.initialize_connection();
        Master.listen_for_commands();

    }


    static void initialize_connection(){

        try {

            serverSocket = new ServerSocket(server_port);

        } catch (IOException e) {
            System.out.println("Master creation error: " + e.toString());
        }
    }


    static void listen_for_commands(){

        try {

            socket = serverSocket.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            String command = reader.readLine();

            System.out.println(command);


        } catch (IOException e) {
            System.out.println("Master socket error: " + e.toString());
        }

    }


    static void terminate_connection(){

        if (serverSocket != null){

            try {

                serverSocket.close();
                Database.save_database();

            } catch (IOException e) {
                System.out.println("Master termination error: " + e.toString());
            }
        }

    }







}
