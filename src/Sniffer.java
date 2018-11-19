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


        showIP(hexData);
        showPort(hexData);

    }

    public static void showIP(String hexData){

        String sourceIP = hexData.substring(52,60);
        String destIP = hexData.substring(61,69);


        System.out.println("Source IP: ");

        for(int i = 0; i < sourceIP.length(); i++)
        {

            if (i % 2 == 0) {
                System.out.print(Integer.parseInt(sourceIP.substring(i, i + 2), 16));
                if (i != sourceIP.length()-2) System.out.print('.');
            }

        }


        System.out.println("Destination IP: ");

        for(int i =0; i < destIP.length(); i++)
        {

            if (i % 2 == 0) {
                System.out.print(Integer.parseInt(destIP.substring(i, i + 2), 16));
                if (i != destIP.length()-2) System.out.print('.');
            }

        }

    }

    public static void showPort(String hexData) {

        String sourcePort =hexData.substring(68,72);
        System.out.println("Source Port: ");
        System.out.print(Integer.parseInt(sourcePort,16));

        String destinationPort =hexData.substring(72,76);
        System.out.println("Destination Port: ");
        System.out.print(Integer.parseInt(destinationPort,16));

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