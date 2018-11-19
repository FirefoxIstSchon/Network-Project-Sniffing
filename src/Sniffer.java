import java.util.ArrayList;
import java.util.Scanner;

public class Sniffer {

    static String hexData = "";

    public static void main(String[] args){

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

        System.out.println(input);

        hexData = eraseSpaces(input);
        System.out.println(hexData);


        findIP(hexData);
        //findPort(hexData);

    }

    public static void findIP(String hexData){

        String sourceIP =hexData.substring(52,60);
        System.out.println("Source IP: ");
        String hex="";
        int len =sourceIP.length();
        for(int i =0; i< len; i++)
        {
            hex=hex+sourceIP.charAt(i);
            System.out.print(Integer.parseInt(hex,16));
            if(i!=len){
                System.out.print(".");
            }
        }

    }

    public static void findPort(String hexData) {



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