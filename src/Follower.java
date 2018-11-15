

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Follower {

    Socket socket;
    BufferedReader reader;
    PrintWriter writer;

    static String SERVER_ADDRESS;
    static int SERVER_PORT;


    public Follower(String SERVER_ADDRESS, int SERVER_PORT){
        this.SERVER_ADDRESS=SERVER_ADDRESS;
        this.SERVER_PORT=SERVER_PORT;

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


            command=sc.nextLine();

            if(command.equals("exit")) {
                follower.terminate_connection();
                break;
            }


            follower.send_command(command);
            str =follower.get_response();
            System.out.flush();



            }



            try {
                Thread.sleep(timeout_ms);
            } catch (InterruptedException e) {
                System.out.println("Master_Connector thread interrupted");
            }

        }

    }



