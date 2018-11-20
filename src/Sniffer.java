import javax.xml.bind.DatatypeConverter;
import java.util.Scanner;

public class Sniffer {

    static String hexData = "";

    public static void main(String[] args){
        //check whether it is an ssl connection or not, since some parts are
        //different in hexdumb.
        boolean isSsl=false;
        Scanner sc=new Scanner(System.in);
        System.out.println("Do you want give SSL or TCP hexdumb?");
        String in=sc.nextLine();
        if (in.contains("ssl")){
            isSsl=true;
        }

        System.out.println("Give hexadecimal input.");

        //getting the multiple line hexadecimal input from user.
        String input = "";
        Scanner keyboard = new Scanner(System.in);

        String line;
        while (keyboard.hasNextLine()) {
            line = keyboard.nextLine();
            if (line.isEmpty()) {
                break;
            }
            input += " "+line ;
        }

        //erasing spaces and converting hex dumb into one string
        //without any space
        hexData = eraseSpaces(input);

        //displaying relevant information from hex dumb
        showIP(hexData);
        showPort(hexData);
        showPayload(hexData, isSsl);
        showData(hexData, isSsl);

    }

    public static void showIP(String hexData){
        //getting relevant parts from hex dumb
        String sourceIP = hexData.substring(52,60);
        String destIP = hexData.substring(60,68);


        System.out.println("Source IP: ");

        for(int i = 0; i < sourceIP.length(); i++)
        {

            if (i % 2 == 0) {
                //converting hexadecimal to string
                System.out.print(Integer.parseInt(sourceIP.substring(i, i + 2), 16));
                if (i != sourceIP.length()-2) System.out.print('.');
            }

        }
        System.out.println();


        System.out.println("Destination IP: ");

        for(int i =0; i < destIP.length(); i++)
        {

            if (i % 2 == 0) {
                //converting hexadecimal to string
                System.out.print(Integer.parseInt(destIP.substring(i, i + 2), 16));
                if (i != destIP.length()-2) System.out.print('.');
            }

        }
        System.out.println();

    }

    public static void showPort(String hexData) {
        //getting relevant parts from hex dumb
        String sourcePort =hexData.substring(68,72);
        System.out.println("Source Port: ");
        //converting hexadecimal to string
        System.out.println(Integer.parseInt(sourcePort,16));

        //getting relevant parts from hex dumb
        String destinationPort =hexData.substring(72,76);
        System.out.println("Destination Port: ");
        //converting hexadecimal to string
        System.out.println(Integer.parseInt(destinationPort,16));

    }
    public static void showPayload (String hexData,boolean isSsl){
        String data="";
        //getting relevant parts from hex dumb
        data =hexData.substring(108,hexData.length());

        System.out.println("TCP payload: "+data.length()/2+" bytes.");




    }
    public static void showData (String hexData,boolean isSsl){
        String data="";
        //check for ssl connection, the data is in different place.
        if (isSsl){
            //getting relevant parts from hex dumb
            data =hexData.substring(118,hexData.length());
        }else{
            //getting relevant parts from hex dumb
            data =hexData.substring(108,hexData.length());
        }


        StringBuilder str = new StringBuilder();


        for (int i = 0; i < data.length(); i+=2) {
            //converting hexadecimal to string
            str.append((char) Integer.parseInt(data.substring(i, i + 2), 16));
        }

        System.out.println(str.toString());




    }

    public static String eraseSpaces(String s){

        String[] temp = s.split("  ");
        String[] raw ;
        String hex="";
        for (int i=0; i<temp.length; i++)
        {
            raw=temp[i].split(" ");

            for(int k=0; k<raw.length; k++)
            {
                if (raw[k].length()!=4)
                {
                    hex=hex+raw[k];
                }

            }

        }


        return hex;
    }


}