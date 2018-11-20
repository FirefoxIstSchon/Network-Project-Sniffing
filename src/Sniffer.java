import javax.xml.bind.DatatypeConverter;
import java.util.Scanner;

public class Sniffer {

    static String hexData = "";

    public static void main(String[] args){
        System.out.println("Give hexadecimal input.");
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


        hexData = eraseSpaces(input);

        showIP(hexData);
        showPort(hexData);
        showData(hexData);

    }

    public static void showIP(String hexData){

        String sourceIP = hexData.substring(52,60);
        String destIP = hexData.substring(60,68);


        System.out.println("Source IP: ");

        for(int i = 0; i < sourceIP.length(); i++)
        {

            if (i % 2 == 0) {
                System.out.print(Integer.parseInt(sourceIP.substring(i, i + 2), 16));
                if (i != sourceIP.length()-2) System.out.print('.');
            }

        }
        System.out.println();


        System.out.println("Destination IP: ");

        for(int i =0; i < destIP.length(); i++)
        {

            if (i % 2 == 0) {
                System.out.print(Integer.parseInt(destIP.substring(i, i + 2), 16));
                if (i != destIP.length()-2) System.out.print('.');
            }

        }
        System.out.println();

    }

    public static void showPort(String hexData) {

        String sourcePort =hexData.substring(68,72);
        System.out.println("Source Port: ");
        System.out.println(Integer.parseInt(sourcePort,16));

        String destinationPort =hexData.substring(72,76);
        System.out.println("Destination Port: ");
        System.out.println(Integer.parseInt(destinationPort,16));

    }
    public static void showPayload (String hexData){

        String data =hexData.substring(108,hexData.length());


        StringBuilder str = new StringBuilder();

        for (int i = 0; i < hexData.length(); i+=2) {
            str.append((char) Integer.parseInt(hexData.substring(i, i + 2), 16));
        }

        System.out.println(str.toString());




    }
    public static void showData (String hexData){

        String data =hexData.substring(148,hexData.length());


        StringBuilder str = new StringBuilder();

        for (int i = 0; i < hexData.length(); i+=2) {
            str.append((char) Integer.parseInt(hexData.substring(i, i + 2), 16));
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